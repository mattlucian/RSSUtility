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
<%@ page import="feeds.Item" %>

<html>
  <head>
    <title>RSSUtility</title>
    <link rel="stylesheet" type="text/css" href="css/site.css"> <!-- CSS -->
    <script type="text/javascript" src="js/site.js"></script>   <!-- JS -->
  </head>
  <body>
    <p style="color:red;margin-top:3%;" id="errorMessage">${sessionScope.errorMessage}</p>
    <% request.getSession().setAttribute("errorMessage","");%>
    <h1 class="heading">RSSUtility</h1>
    <button onclick="visitPage('http://github.com/mattlucian/RSSUtility')" class="button-style">Source Code</button>
    <button onclick="visitPage('https://github.com/mattlucian/RSSUtility/blob/master/TODO.md')" class="button-style">TODO List</button>
    <hr class="half-hr" />


    <!-- ADD RSS FEED FORM -->
    <div style="width:100%;float:left">
        <div style="width:500px;margin:auto">
            <h3 class="heading">Add a new RSS Feed</h3>
            <form action="/TrackerServlet" method="post" name="add">
                <div class="input-container">
                    <label class="default-label" for="newFeedName">Name</label>
                    <input id="newFeedName" name="name" type="text" placeholder="NY Times">
                </div>

                <div class="input-container">
                    <label class="default-label" for="newFeedUrl">URL</label>
                    <input id="newFeedUrl" name="url" type="text" placeholder="http://rss.nytimes.com/services/xml/rss/nyt/Americas.xml">
                </div>

                <input type="hidden" name="add" />
                <div class="input-container">
                    <input style="float:right" class="button-style" type="submit" value="Add RSS" >
                </div>
            </form>
        </div>
    </div>
    <hr class="half-hr" />


    <!-- CURRENT RSS FEEDS -->
    <div style="width:100%;float:left">
        <div style="width:500px;margin:auto;">
            <h2 class="rss-heading">RSS Feeds</h2>
            <form id="rssForm" action="/TrackerServlet" method="post">
                <input type="hidden" id="display" name="display" value="" />
                <input type="hidden" id="delete" name="delete" value="" />
                <input type="hidden" id="servlet_plan" name="plan" value="" />
                <input type="hidden" id="currentSelection" name="currentSelection" value=""/>
                <table id="feedTable">
                    <%
                        Utility utility = new Utility();
                        try{
                            ArrayList<Feed> feeds = utility.getFeeds();
                            for(Feed feed : feeds) {
                                out.println(feed.getFeedHtml());
                            }
                        }catch(Exception ex){

                        } %>

                    <!-- JSTL custom function that returns Feed HTML -->

                </table>
                <hr class="full-hr" />
                <div style="text-align:right;width:500px;margin:auto;">
                    <button class="button-style" onclick="exportAll()">Export All</button>
                </div>
            </form>
        </div>
    </div>



    <!-- DISPLAY RSS -->
    <%
        ArrayList<Item> items = null;
        if((items = (ArrayList<Item>)session.getAttribute("feedItems")) != null){

            for(Item item : items){

            }


        }
    %>

    <!-- RSS DISPLAY TEMPLATE -->
    <jsp:include page="rssDisplayTemplate.jsp" />

    <!-- EXPORT RSS -->
    <form action="/TrackerServlet" id="exportForm" method="post" >
        <input type="hidden" id="export" name="export" value="" />
    </form>



  </body>
</html>
