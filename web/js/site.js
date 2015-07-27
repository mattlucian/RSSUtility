/**
 * Created by matt on 7/26/15.
 */
function clickedDisplay(id){
    var displayurl = document.getElementById("url_"+id).value;
    document.getElementById("display").value = displayurl;
    document.getElementById("servlet_plan").value = "display";
    document.getElementById("rssForm").submit();
}

function clickedExport(id){
    document.getElementById("export").value = document.getElementById("url_"+id).value;
    document.getElementById("servlet_plan").value = "export";
    document.getElementById("currentSelection").value = id;
    document.getElementById("exportForm").submit();
}

function exportAll(){
    document.getElementById("servlet_plan").value = "exportAll";
    document.getElementById("rssForm").submit();
}