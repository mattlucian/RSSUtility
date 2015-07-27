# RSSTracker
Simple Java Servlet application that tracks RSS feeds and contains exporting capability (CSV)

Core
-----
  - TrackerServlet class
    - Processes Add Request (New Feed)
    - Processes Export Request (Export Single Feed)
    - Processes ExportAll Request (Export All Feeds)

  - Utility class
    - GetFeeds method  : returns all current feeds in DB
    - SerializeFeed : Serializes single feed for CSV

  - ServletStatus class
    - Provides for more detailed responses with error messages

  - Feed class
    - Holder for the Feed information from DB

  - index.jsp
    - Displays feeds and the ability to add feeds.
  - error.jsp
    - Landing page when an unexpected error occurs

Technology
-----
  - IDE
    - JetBrains IntelliJ IDEA 14 (Ultimate)
    - Servlet API

  - Server
    - Java 1.7
    - Tomcat 7.0
    - Tomcat Controller for OSX
    - MySQL
    - MySQL -> Tomcat J Connector
    - JSTL 1.2
