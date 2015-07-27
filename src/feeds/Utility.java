package feeds;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
            //String connectionURL = "jdbc:mysql://localhost/rssutility";
            Connection connection = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(connectionURL, "alpin3_1", "passw0rd");
            //connection = DriverManager.getConnection(connectionURL, "root", "admin");

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

   /* public ArrayList<Item> getFeedItems(int id){
        ArrayList<Item> items = new ArrayList<Item>();
        String url = "";
        try {
            String connectionURL = "jdbc:mysql://mysql3000.mochahost.com/alpin3_rssutility";
            //String connectionURL = "jdbc:mysql://localhost/rssutility";
            Connection connection = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(connectionURL, "alpin3_1", "passw0rd");
            //connection = DriverManager.getConnection(connectionURL, "root", "admin");

            if(!connection.isClosed()){
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM rssfeed WHERE id = ?");
                stmt.setString(1,""+id);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    url = rs.getString("url");
                }



            }
            connection.close();

        }catch(Exception ex){
            return new ArrayList<Item>();
        }
    }*/

    public String serializeFeed(String currentCSV, NodeList nList, String source) {
        for (int temp = 0; temp < nList.getLength(); temp++) { // FOR EACH ITEM in RSS Feed

            Element eNode = (Element)nList.item(temp);
            currentCSV += source.replace(',',';').replace('\n',' ')+ ","; // Adds Source column
            currentCSV += serializeItem(eNode);
        }
        return  currentCSV;

    }

    public String serializeFeed(String currentCSV, NodeList nList){
        for (int temp = 0; temp < nList.getLength(); temp++) { // FOR EACH ITEM in RSS Feed

            Element eNode = (Element)nList.item(temp);
            currentCSV += serializeItem(eNode);
        }
        return  currentCSV;
    }

    public String serializeItem(Element eNode){
        String newLine = "";
        newLine += eNode.getElementsByTagName("title").item(0).getTextContent().replace(',',';').replace('\n',' ')+ ",";
        newLine += eNode.getElementsByTagName("pubDate").item(0).getTextContent().replace(',', ';').replace('\n',' ') + ",";
        newLine += eNode.getElementsByTagName("category").item(0).getTextContent().replace(',', ';').replace('\n',' ') + ",";
        newLine += eNode.getElementsByTagName("description").item(0).getTextContent().replace(',',';').replace('\n',' ');
        newLine += "\n";
        return  newLine;
    }


}
