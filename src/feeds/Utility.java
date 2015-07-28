package feeds;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by matt on 7/26/15.
 */
public class Utility {
    // *****************
    private String connectionURL = "";
    private String connectionUserName = "";
    private String connectionPassword = "";
    /// ***************

    public Utility(String con, String user, String pass){
        connectionURL = con;
        connectionUserName = user;
        connectionPassword = pass;
    }
    public Utility(){
        try{
            Context env = (Context)new InitialContext().lookup("java:comp/env");
            connectionURL = (String)env.lookup("connectionString");
            connectionUserName = (String)env.lookup("username");
            connectionPassword = (String)env.lookup("password");

        }catch (Exception ex){
            // error
        }

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

    public ArrayList<Item> getFeedItems(String url){
        ArrayList<Item> items = new ArrayList<Item>();
        try {
            InputStream inputXML = new URL(url).openConnection().getInputStream();

            // XML reader
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(inputXML);

            // Get list of all the items from Feed
            NodeList nList = doc.getElementsByTagName("item");

            for(int i = 0; i < nList.getLength(); i++){
                Element eNode = (Element)nList.item(i);
                items.add(new Item(

                ));
            }

        }catch (Exception ex){
            return null;
        }
        return items;
    }

    public String serializeFeed(String currentCSV, ArrayList<Item> items, String source) {
        for (Item item : items) { // FOR EACH ITEM in RSS Feed
            currentCSV += source.replace(',',';').replace('\n',' ')+ ","; // Adds Source column
            currentCSV += serializeItem(item);
        }
        return  currentCSV;

    }

    public String serializeFeed(String currentCSV, ArrayList<Item> items){
        for (Item item : items) { // FOR EACH ITEM in RSS Feed
            currentCSV += serializeItem(item);
        }
        return  currentCSV;
    }

    public String serializeItem(Item item){
        String title = "", pubDate = "", category = "", description = "";
        // title
        try{
            title = parseAttributeForCSV(item.title) + ",";
        }catch (Exception ex){
            title = "Not Found ,";
        }
        // publication date
        try{
            pubDate = parseAttributeForCSV(item.publicationDate) + ",";
        }catch (Exception ex){
            pubDate = "Not Found ,";
        }
        // category
        try{
            category = parseAttributeForCSV(item.category) + ",";
        }catch (Exception ex){
            category = "Not Found ,";
        }
        // description
        try {
            description = parseAttributeForCSV(item.description);
        }catch (Exception e){
            description = "Not Found";
        }
        return  title + pubDate + category + description + "\n";
    }

    public ArrayList<Item> parseXMLToItems(NodeList nodes){
        ArrayList<Item> items = new ArrayList<Item>();
        for(int i = 0; i < nodes.getLength(); i++){
            Item item = new Item();
            Element element = (Element)nodes.item(i);

            try{
                item.title = parseElementFromXML(element,"title");
            }catch (Exception ex){
                item.title = "N/A";
            }
            try{
                item.publicationDate = parseElementFromXML(element,"pubDate");
            }catch (Exception ex){
                item.publicationDate = "N/A";
            }
            try{
                item.category = parseElementFromXML(element,"category");
            }catch (Exception ex){
                item.category = "N/A";
            }
            try{
                item.description = parseElementFromXML(element,"description");
            }catch (Exception ex){
                item.description = "N/A";
            }

            items.add(item);

        }
        return items;
    }

    public String parseAttributeForCSV(String itemAttribute){
        return itemAttribute.replace(',',';').replace('\n',' ');
    }
    public String parseElementFromXML(Element e, String tag){
        return e.getElementsByTagName(tag).item(0).getTextContent().trim();
    }

}
