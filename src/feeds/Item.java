package feeds;

/**
 * Created by matt on 7/27/15.
 */
public class Item {
    public String title = "";
    public String publicationDate = "";
    public String category = "";
    public String description = "";

    Item(){

    }

    Item(String title, String publicationDate, String category, String description){
        this.title = title;
        this.publicationDate = publicationDate;
        this.category = category;
        this.description = description;
    }
}
