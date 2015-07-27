<%--
  Created by IntelliJ IDEA.
  User: matt
  Date: 7/18/15
  Time: 11:58 PM
--%>
<%@ page
        import="feeds.Feed"
        import="feeds.Utility"
        import="java.util.ArrayList"
        contentType="text/html;charset=UTF-8" language="java"
%>

<html>
  <head>
    <title>RSSUtility</title>
    <link rel="stylesheet" type="text/css" href="css/site.css"> <!-- CSS -->
    <script type="text/javascript" src="js/site.js"></script>   <!-- JS -->
  </head>
  <body>
    <p style="color:red">${sessionScope.errorMessage}</p>
    <% request.getSession().setAttribute("errorMessage","");%>
    <h1>Welcome to RSSUtility</h1>
    <a href="http://github.com/mattlucian/RSSUtility" target="_blank"><h4>Source Code</h4></a>

    <!-- ADD RSS FEED FORM -->
    <h5>Add a new RSS Feed</h5>
    <form action="/TrackerServlet" method="post" name="add">
      Label: <input name="name" type="text" placeholder="Yahoo Tech News"> <BR>
      RSS Url: <input name="url" type="text" placeholder="http://news.yahoo.com/rss/tech"> <BR>
      <input type="hidden" name="add" />
      <input type="submit" value="Add RSS" >
    </form>

    <!-- CURRENT RSS FEEDS -->
    <h2>RSS Feeds</h2>
    <form id="rssForm" action="/TrackerServlet" method="post">
        <button onclick="exportAll()">Export All</button>
        <input type="hidden" id="display" name="display" value="" />
        <input type="hidden" id="servlet_plan" name="plan" value="" />
        <input type="hidden" id="currentSelection" name="currentSelection" value=""/>
        <table>
            <tr>
                <th>RSS Feed</th>
                <th>Action</th>
            </tr> <%
             Utility utility = new Utility();
             try{
                 ArrayList<Feed> feeds = utility.getFeeds();
                 for(Feed feed : feeds) {
                   out.println(feed.getFeedHtml());
                 }
             }catch(Exception ex){

             } %>
        </table>
    </form>


    <!-- RSS DISPLAY TEMPLATE -->
    <jsp:include page="rssDisplayTemplate.jsp" />

    <!-- EXPORT RSS -->
    <form action="/TrackerServlet" id="exportForm" method="post" >
        <input type="hidden" id="export" name="export" value="" />
        <button id="weirdBugFix" onclick="clickedExport(1)" >Test</button>
    </form>



  </body>
</html>
