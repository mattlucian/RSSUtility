// Import required java libraries
import java.io.*;


import java.net.URL;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import feeds.Feed;
import feeds.Utility;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;


// Extend HttpServlet class
public class TrackerServlet extends HttpServlet {

    private Connection connection = null;

    public void init() throws ServletException
    {
        try{ // Do required initialization
            String connectionURL = "jdbc:mysql://mysql3000.mochahost.com/alpin3_rssutility";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.connection = DriverManager.getConnection(connectionURL, "alpin3_1", "passw0rd");

        }catch(Exception ex){
            // unable to hit the database

        }
}

    // Handles POST requests, only thing currently supported
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException
    {
        ServletStatus status = new ServletStatus();

        /*if(failed){ // check DB error
            failed = false;
            response.sendRedirect("error.jsp");
            destroy();
        }
*/
        String action = null;
        try {
            if((action = request.getParameter("plan")) != null){

                if(action.equals("export")){
                    status = export(request,response);
                    if(!status.succeeded){
                        // error exporting single, please try again.
                        request.getSession().setAttribute("errorMessage", status.message);
                        response.sendRedirect("index.jsp");
                    }


                }else if(action.equals("display")){

                    response.sendRedirect("index.jsp?display="+request.getParameter("display"));

                }else if(action.equals("exportAll")){
                    status = exportAll(request,response);
                    if(!status.succeeded){
                        // error exporting all, please try again
                    }
                }

            }

            if(request.getParameter("add") != null){

                status = add(request,response);

                if(status.succeeded) {
                    response.sendRedirect("index.jsp");
                }else{
                    // send to index but pass error message
                    request.getSession().setAttribute("errorMessage", status.message);
                    response.sendRedirect("index.jsp");
                }

            }

        }catch (Exception ex){

            request.getSession().setAttribute("errorMessage","Unable to perform operation, please try again.");
            response.sendRedirect("index.jsp");
        }


    }

    // Add new Feed
    public ServletStatus add(HttpServletRequest request, HttpServletResponse response) throws IOException{
        // Set response content type
        response.setContentType("text/html");
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

            Utility utility = new Utility();
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

                // Serialize NodeList to CSV
                CSVExport = utility.serializeFeed(CSVExport,nList);

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
    public ServletStatus exportAll(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        ServletStatus status = new ServletStatus();

        Utility utility = new Utility();
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

                // Serialize NodeList to CSV
                CSVExport = utility.serializeFeed(CSVExport, nList, feed.feedName);

            } catch (Exception e) {
                status.message = "inner fail "+e.getMessage();
                status.succeeded = false;
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