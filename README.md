# RSSTracker
Simple Java Servlet application that tracks RSS feeds and contains exporting capability

Core
-----
  - TrackerServlet class
    - Processes AddFeed Request
    - Processes ExportFeed Request //todo
    - Processes ExportFeeds Request //todo

  - Utility class
    - GetFeeds method  : returns all current feeds in DB
    - ExportFeed(Feed) : exports one feed to CSV //todo

  - ServletStatus class
    - Provides for more detailed responses

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
