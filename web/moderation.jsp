<%-- 
    Document   : index
    Created on : Jan 5, 2014, 4:43:34 PM
    Author     : paolo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Student's Forum | Moderation</title>
        <%@include file="include/head.jsp" %>
    </head>
    <body>
        <div id="moderation-page" data-role="page">
            <div data-role="header">
                <a href="#panel" data-icon="bars">Menu</a>
                <h2>Student's Forum | Moderation</h2>
            </div>
            <div data-role="content">
                <c:if test="${error != null}">
                    <div class="ui-body ui-body-b">
                        ${error}
                    </div>
                </c:if>
                <form method="post" onsubmit="return confirm('Are you sure? This action can\'t be undone')" data-ajax="false">
                    <table id="moderation-table">
                        <thead>
                        <th></th>
                        <th>Creator</th>
                        <th>Group name</th>
                        <th>Public?</th>
                        <th>Members</th>
                        <th>Posts</th>
                        <th></th>
                        </thead>

                        <tbody>
                            <c:forEach items="${groups}" var="g">
                                <tr>
                                    <td style="width: 50px">
                                        <img src="/avatar?id=${g.getCreator()}" style="width: auto; height: 50px">
                                    </td>
                                    <td>
                                        <b>${dbmanager.getUser(g.getCreator()).getName()}</b>
                                    </td>
                                    <td>
                                        <a href="/group-posts?id=${g.getId()}">${g.getName()}</a>
                                    </td>
                                    <td style="width: 3em">
                                        ${g.isPublic() ? "Yes" : "No"}
                                    </td>
                                    <td style="width: 3em">
                                        ${g.getUserCount()}
                                    </td>
                                    <td style="width: 3em">
                                        ${g.getPostsCount()}
                                    </td>
                                    <td style="text-align: right; width: 6em">
                                        <button type="submit" name="group" value="${g.getId()}" ${g.isClosed() ? "disabled" : ""} data-mini="true" data-icon="delete" data-inline="true" data-theme="a">${g.isClosed() ? "Closed" : "Close"}</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </form>
                <script>
                    $("#moderation-table").dataTable();
                    $("#moderation-table_wrapper").find("select, input").attr("data-role", "none");
                </script>
                <%@include file="include/panel.jsp" %>
            </div>
    </body>
</html>
