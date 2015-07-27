/**
 * Created by matt on 7/26/15.
 */
package feeds;

import java.util.ArrayList;

public class Feed {
    public int id;
    public String feedName;
    public String feedURL;
    public ArrayList<Item> items = null;

    public Feed(int id, String feed, String url){
        this.id = id;
        this.feedName = feed;
        this.feedURL = url;
    }

    public String getFeedHtml(){
        String html = "";
        html += "<tr><td>"+feedName+"</td><td style=\"text-align:right;\"><button class=\"button-style\" onclick=\"clickedDisplay("+id+")\">Display</button>";
        html += "<button class=\"button-style\" onclick=\"clickedExport("+id+")\">Export</button>";
        html += "<input type=\"hidden\" name=\"url_"+id+"\" id=\"url_"+id+"\" value=\""+feedURL+"\" /></td></tr>";
        return html;
    }

}
