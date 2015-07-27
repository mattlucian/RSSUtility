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
    private String connectionURL = "***************";
    private String connectionUserName = "***************";
    private String connectionPassword = "***************";
    /// ***************

    public Utility(){

    }

    public ArrayList<Feed> getFeeds(){
        ArrayList<Feed> feeds = new ArrayList<Feed>();
        try {
            //String connectionURL = "jdbc:mysql://localhost/rssutility";
            Connection connection = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(connectionURL, connectionUserName, connectionPassword);
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

    public ArrayList<Item> getFeedItems(int id){
        ArrayList<Item> items = new ArrayList<Item>();
        String url = "";
        try {
            //String connectionURL = "jdbc:mysql://localhost/rssutility";
            Connection connection = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(connectionURL, connectionUserName, connectionPassword);
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

        return items;
    }

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
        String title = "", pubDate = "", category = "", description = "";
        // title
        try{
            title = eNode.getElementsByTagName("title").item(0).getTextContent().replace(',',';').replace('\n',' ')+ ",";
        }catch (Exception ex){
            title = "Not Found ,";
        }
        // publication date
        try{
            pubDate = eNode.getElementsByTagName("pubDate").item(0).getTextContent().replace(',', ';').replace('\n',' ') + ",";
        }catch (Exception ex){
            pubDate = "Not Found ,";
        }
        // category
        try{
            category = eNode.getElementsByTagName("category").item(0).getTextContent().replace(',', ';').replace('\n',' ') + ",";
        }catch (Exception ex){
            category = "Not Found ,";
        }
        // description
        try {
             description = eNode.getElementsByTagName("description").item(0).getTextContent().replace(',',';').replace('\n',' ');
        }catch (Exception e){
            description = "Not Found";
        }

        String newLine = title + pubDate + category + description + "\n";
        return  newLine;
    }


}
