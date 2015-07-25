// Import required java libraries
import java.io.*;
import java.net.URL;
import java.sql.*;
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

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException
    {
        if(failed){ // check DB error
            failed = false;
            response.sendRedirect("error.jsp");
            destroy();
        }

        try {
            if(request.getParameter("export") != null){

                export(request,response);

            }else if(request.getParameter("add") != null){

                add(request,response);

            }else if(request.getParameter("select") != null){



            }
        }catch (Exception ex){

        }


    }

    public void export(HttpServletRequest request, HttpServletResponse response) throws IOException{

    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws IOException{
        // Set response content type
        response.setContentType("text/html");

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
            response.sendRedirect("error.jsp");
        }

        //PrintWriter p = response.getWriter();
        //p.println("name: "+feedName + "  url: "+feedURL);
//        PrintWriter p = response.getWriter();

        // Success
        try{
            if(connection == null) {
                response.sendRedirect("error.jsp");
                //p.println("connection isnt here");
            }
            if(!connection.isClosed()) {
                // add rss entry
//                p.println("connection is open");

                PreparedStatement validateStmt = connection.prepareStatement("SELECT name, url FROM rssfeed WHERE name = ? OR url = ? ");
                validateStmt.setString(1, feedName);
                validateStmt.setString(2, feedURL);
                ResultSet rs = validateStmt.executeQuery();
                if (rs != null) {
                    if (rs.next()) {
                        response.sendRedirect("alreadyexists.jsp");
                    }else{

                        PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO rssfeed (name, url) values ( ? , ? )");
                        insertStmt.setString(1,feedName);
                        insertStmt.setString(2, feedURL);
                        insertStmt.execute();

                        response.sendRedirect("index.jsp");

                    }
                }
            }
        }catch(Exception ex){
            response.sendRedirect("error.jsp");
        }
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