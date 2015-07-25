# RSSTracker
Simple Java Servlet application that tracks RSS feeds and contains exporting capability

Core
-----
  - TrackerServlet class
    - Processes GetFeeds Request //todo
    - Processes AddFeed Request
    - Processes ExportFeed Request //todo
    - Processes ExportFeeds Request //todo
  - index.jsp
    - Displays feeds and the ability to add feeds.
  - error.jsp
    - Landing page when an error occurs
  - alreadyexists.jsp
    - Landing page when trying to add a feed that already exists
      - (May remove and add error messages to index.jsp to reduce pages)

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
