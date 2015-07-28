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

function visitPage(url){
    var win = window.open(url, '_blank');
    win.focus();
}

function setErrorMessage(message){
    document.getElementById("errorMessage").innerText = message;
}

function clickedDelete(id){
    var result = confirm("Are you sure you'd like to delete this?");
    if(result == true){
        document.getElementById("servlet_plan").value = "delete";
        document.getElementById("delete").value = id;
    }
}