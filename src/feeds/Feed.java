/**
 * Created by matt on 7/26/15.
 */
package feeds;

public class Feed {
    public int id;
    public String feedName;
    public String feedURL;

    public Feed(int id, String feed, String url){
        this.id = id;
        this.feedName = feed;
        this.feedURL = url;
    }

    public String getFeedHtml(){
        String html = "";
        html += "<tr><td>"+feedName+"</td><td><button onclick=\"clickedDisplay("+id+")\">Display</button>";
        html += "<input type=\"hidden\" id=\"url_"+id+"\" value=\""+feedURL+"\" /></td></tr>";
        return html;
    }
}
