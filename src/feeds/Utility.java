package feeds;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by matt on 7/26/15.
 */
public class Utility {
    public Utility(){

    }

    public ArrayList<Feed> getFeeds(){
        ArrayList<Feed> feeds = new ArrayList<Feed>();
        try {
            String connectionURL = "jdbc:mysql://mysql3000.mochahost.com/alpin3_rssutility";
            Connection connection = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(connectionURL, "alpin3_1", "passw0rd");

            if(!connection.isClosed()){
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM rssfeed");
                ResultSet rs = stmt.executeQuery();
                while(rs.next()){
                    feeds.add(new Feed(rs.getInt("id"),rs.getString("name"),rs.getString("url")));
                }
            }
            connection.close();
            return feeds;
        }catch(Exception ex){
            return new ArrayList<Feed>();
        }

    }

}
