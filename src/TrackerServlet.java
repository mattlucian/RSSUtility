// Import required java libraries
import java.io.*;

import feeds.Feed;
import java.sql.*;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;

// Extend HttpServlet class
public class TrackerServlet extends HttpServlet {

    private boolean failed = false;
    private Connection connection = null;

    public void init() throws ServletException
    {
        try{ // Do required initialization
            String connectionURL = "jdbc:mysql://mysql3000.mochahost.com/alpin3_rssutility";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.connection = DriverManager.getConnection(connectionURL, "alpin3_1", "passw0rd");

        }catch(Exception ex){
            // unable to hit the database
            failed = true;
        }
}

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException
    {
        ServletStatus status = new ServletStatus();

        if(failed){ // check DB error
            failed = false;
            response.sendRedirect("error.jsp");
            destroy();
        }

        try {
            if(request.getParameter("export") != null){

                status = export(request,response);
                if(status.succeeded){

                }else{
                    // failed
                }

            }else if(request.getParameter("add") != null){

                status = add(request,response);

                if(status.succeeded) {
                    response.sendRedirect("index.jsp");
                }else{
                    // send to index but pass error message
                    request.getSession().setAttribute("errorMessage", status.message);
                    response.sendRedirect("index.jsp");
                    //request.setAttribute("errorMessage",status.message);
                    //RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
                    //dispatcher.forward(request, response);
                }

            }
        }catch (Exception ex){

            request.getSession().setAttribute("errorMessage","Unable to perform operation, please try again.");
            response.sendRedirect("index.jsp");
            //RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
            //dispatcher.forward(request, response);
        }


    }

    // export to CSV
    public ServletStatus export(HttpServletRequest request, HttpServletResponse response) throws IOException{
        ServletStatus status = new ServletStatus();

        return status;
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
                failed = true;
            }
        }catch (Exception ex){
            status.succeeded = false;
            status.message = "Please enter correct name and URL";
        }

        // Success
        try{
            if(connection == null) {
                status.succeeded = false;
                status.message = "Failed to connect to database";
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
                        status.message = "Name or URL already exists, please try again";
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
                    status.message = "Failed to connect to database";
                }
            }else{
                status.succeeded = false;
                status.message = "Database connection already closed, please try again.";
            }
        }catch(Exception ex){
            status.succeeded = false;
            status.message = "Failure while trying to insert new feed, please try again\n";
            status.message += ex.getMessage();

        }
        return status;
    }

    public void destroy()
    {
        // do nothing.
        try{
            connection.close();
        }catch (SQLException ex){
            failed = true;
        }

    }



}