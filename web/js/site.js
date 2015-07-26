/**
 * Created by matt on 7/26/15.
 */
function clickedDisplay(id){
    var url = document.getElementById("url_"+id).value;
    document.getElementById("feedURL").value = url;
    document.getElementById("rssForm").submit();
}
