<%--
  Created by IntelliJ IDEA.
  User: matt
  Date: 7/26/15
  Time: 6:00 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- JSTL display for RSS feeds -->
<c:if test="${(param.display != null) && (param.display != '')}">

  <h3>Displaying for: ${param.display}</h3>

  <c:import var="xmlContent" url="${param.display}"/>
  <x:parse var="doc" xml="${xmlContent}"/>

  <table class="content-table" id="feed">
    <tr class="profile_odd">
      <td align="center" colspan="2">
            <span class="title">
               <x:out select="$doc/rss/channel/title" />
            </span>
      </td>
    </tr>
    <x:forEach var="story"
               select="$doc/rss/channel/item" varStatus="status">
      <tr>
        <td colspan="2"> <hr/> </td>
      </tr>
      <tr class="profile_even">
        <td class="label">Topic</td>
        <td> <x:out select="title" /> </td>
      </tr>
      <tr class="profile_even">
        <td class="label">Published Date</td>
        <td> <x:out select="pubDate" /> </td>
      </tr>
      <tr class="profile_even">
        <td class="label">Category</td>
        <td> <x:out select="category" /> </td>
      </tr>
      <tr class="" valign="top">
        <td class="label">Description</td>
        <td><x:out select="description" escapeXml="false"/></td>
      </tr>
    </x:forEach>
  </table>
</c:if>
