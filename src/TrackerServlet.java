// Import required java libraries
import java.io.*;


import java.net.URL;
import java.sql.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import feeds.Feed;
import feeds.Item;
import feeds.Utility;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.util.ArrayList;


public class TrackerServlet extends HttpServlet {

    private Connection connection = null;
    private String connectionString = "";
    private String password = "";
    private String username = "";

    public void init() throws ServletException
    {
        try{
            Context env = (Context)new InitialContext().lookup("java:comp/env");
            connectionString = (String)env.lookup("connectionString");
            username = (String)env.lookup("username");
            password = (String)env.lookup("password");

        }catch (Exception ex){
            destroy();
        }
    }

    public ServletStatus delete(HttpServletRequest request, HttpServletResponse response){
        ServletStatus status = new ServletStatus();

        String one="",two="",three="";
        try{
            PreparedStatement validateStmt = connection.prepareStatement("DELETE FROM rssfeed WHERE id = ?");
            validateStmt.setInt(1, Integer.parseInt(request.getParameter("delete")));
            two=" two ";
            validateStmt.execute();
            three = " three ";

            status.succeeded = true;
            status.message = "Removed Feed from DB";
            return status;

        }catch (Exception ex){
            status.succeeded = false;
            status.message = "Unable to remove entry from DB, please try again "+one+two+three;
            return  status;
        }
    }

    // Handles POST requests, only thing currently supported
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException
    {

        try{ // Do required initialization


            /*
               NOTE: Re-establishing a connection every servlet call. For some reason, my host times out the connection
                     very quickly, so this was the simplest solution for the purposes of this application.
             */
            if(connection != null){
                if(connection.isClosed()){
                    connection = null;
                }else{
                    connection.close();
                    connection = null;
                }
            }
            // **************
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.connection = DriverManager.getConnection(connectionString, username, password);

        }catch(Exception ex){
            // unable to hit the database

        }

        ServletStatus status = new ServletStatus();

        String action = null;
        try {
            if((action = request.getParameter("plan")) != null){

                if(action.equals("delete")){ // delete


                    status = delete(request,response);
                    if(!status.succeeded){
                        request.getSession().setAttribute("errorMessage", status.message);
                    }
                    response.sendRedirect("index.jsp");

                }else if(action.equals("export")){ //export
                    status = export(request,response);
                    if(!status.succeeded){
                        // error exporting single, please try again.
                        request.getSession().setAttribute("errorMessage", status.message);
                        response.sendRedirect("index.jsp");
                    }


                }else if(action.equals("display")){ //display

                    /*Utility utility = new Utility();
                    String id = request.getParameter("display");

                    ArrayList<Item> items = utility.getFeedItems(Integer.parseInt(id));
                    request.getSession().setAttribute("feedItems",items);*/

                    // Decided to keep JSTL functionality for demonstration, this allows template to display it
                    response.sendRedirect("index.jsp?display=" + request.getParameter("display"));

                }else if(action.equals("exportAll")){ //export all
                    status = exportAll(response);
                    if(!status.succeeded){
                        // error exporting all, please try again
                    }
                }else if(action.equals("cancel")){
                    response.sendRedirect("index.jsp");
                }

            }else{
                if(request.getParameter("add") != null){


                    response.setContentType("text/html");
                    status = add(request);

                    if(status.succeeded) {
                        response.sendRedirect("index.jsp");
                    }else{
                        // send to index but pass error message
                        request.getSession().setAttribute("errorMessage", status.message);
                        response.sendRedirect("index.jsp");
                    }

                }
            }



        }catch (Exception ex){

            request.getSession().setAttribute("errorMessage","Unable to perform operation, please try again.");
            response.sendRedirect("index.jsp");
        }


    }

    // Add new Feed
    public ServletStatus add(HttpServletRequest request) throws IOException{
        // Set response content type
        ServletStatus status = new ServletStatus();

        // Get parameters
        String feedName = "";
        String feedURL = "";
        try{
            feedName = request.getParameter("name");
            feedURL = request.getParameter("url");
            if(feedName.isEmpty() || feedURL.isEmpty()){ // validate
                status.succeeded = false;
                status.message = "Name and/or URL are empty, please try again.";
                return status;
            }
        }catch (Exception ex){
            status.succeeded = false;
            status.message = "Please enter correct name and URL";
            return status;
        }


        // validate RSS validity
        try{
            Utility utility = new Utility(connectionString,username,password);
            ArrayList<Item> items = utility.getFeedItems(feedURL);
            if(items == null){
                status.message = "Not a supported RSS url, please try again.";
                status.succeeded = false;
                return status;
            }

        }catch (Exception ex){
            status.message = "Not a supported RSS url, please try again.";
            status.succeeded = false;
            return status;
        }


        // Success
        try{
            if(connection == null) {
                status.succeeded = false;
                status.message = "Failed to connect to database";
                return status;
            }
            else if(!connection.isClosed()) {
                // validate that RSS feed doesn't already exist
                PreparedStatement validateStmt = connection.prepareStatement("SELECT name, url FROM rssfeed WHERE name = ? OR url = ? ");
                validateStmt.setString(1, feedName);
                validateStmt.setString(2, feedURL);
                ResultSet rs = validateStmt.executeQuery();

                if (rs != null) {
                    if (rs.next()) {
                        status.succeeded = false;
                        status.message = "Name or URL already exists, please try again\n";
                        return status;
                    }else{
                        // insert feed into DB
                        PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO rssfeed (name, url) values ( ? , ? )");
                        insertStmt.setString(1,feedName);
                        insertStmt.setString(2, feedURL);
                        insertStmt.execute();
                        status.succeeded = true;
                    }
                }else{
                    status.succeeded = false;
                    status.message += "Failed to connect to database\n";
                    return status;
                }
            }else{
                status.succeeded = false;
                status.message += "Database connection already closed, please try again.\n";
                return status;
            }
        }catch(Exception ex){
            status.succeeded = false;
            status.message += "Failure while trying to insert new feed, please try again\n";
            return status;

        }
        return status;
    }

    // export single feed
    public ServletStatus export(HttpServletRequest request, HttpServletResponse response) throws IOException{
        ServletStatus status = new ServletStatus();

        // Get parameters
        String feedURL = "";
        try{
            feedURL = request.getParameter("url_"+request.getParameter("currentSelection"));
            if(feedURL.isEmpty()){ // validate
                status.succeeded = false;
                status.message = "URL parameter is blank, please try again.";
                return status;
            }

            Utility utility = new Utility(connectionString,username,password);
            String CSVExport = "";
            // Parse XML components
            try {
                InputStream inputXML = new URL(feedURL).openConnection().getInputStream();

                // XML reader
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

                Document doc = dBuilder.parse(inputXML);

                // Get list of all the items from Feed
                NodeList nList = doc.getElementsByTagName("item");

                // CSV Header
                CSVExport += "Title,Published Date,Category,Description\n";

                // Convert XML to Items
                ArrayList<Item> items = utility.parseXMLToItems(nList);

                // Serialize NodeList to CSV
                CSVExport = utility.serializeFeed(CSVExport,items);

                // Set up for File download as CSV
                response.setContentType("application/csv");
                response.setHeader("Content-Type", "text/csv");
                response.setHeader("Content-Disposition", "attachment;filename=\"FeedExport.csv\"");

                // Print CSV contents out
                PrintWriter p = response.getWriter();
                p.println(CSVExport);
                p.flush();
                p.close();

                status.succeeded = true;
                return status;

            } catch (Exception e) {
                status.message += "Currently unable to export this type of RSS feed.";
                status.succeeded = false;
            }


        }catch (Exception ex){
            status.succeeded = false;
            status.message = "Failed to get Name and/or URL parameters, please try again.";
        }

        return status;
    }

    // export all feeds
    public ServletStatus exportAll(HttpServletResponse response) throws IOException
    {
        ServletStatus status = new ServletStatus();

        Utility utility = new Utility(connectionString,username,password);
        ArrayList<Feed> feeds = utility.getFeeds();


        // CSV Header
        String CSVExport =  "Source,Title,Published Date,Category,Description\n";

        for(Feed feed : feeds){
            try {
                // XML reader
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(feed.feedURL);

                // Get list of all the items from Feed
                NodeList nList = doc.getElementsByTagName("item");

                //convert list to items
                ArrayList<Item> items = utility.parseXMLToItems(nList);

                // Serialize NodeList to CSV
                CSVExport = utility.serializeFeed(CSVExport, items, feed.feedName);

            } catch (Exception e) {
                // skip this feed
            }

        }

        // Set up for File download as CSV
        response.setContentType("application/csv");
        response.setHeader("Content-Type", "text/csv");
        response.setHeader("Content-Disposition", "attachment;filename=\"ExportAll.csv\"");

        // Print CSV contents out
        PrintWriter p = response.getWriter();
        p.println(CSVExport);
        p.flush();
        p.close();

        status.succeeded = true;
        return status;


    }

    public void destroy()
    {
        // do nothing.
        try{
            connection.close();
        }catch (SQLException ex){

        }

    }



}