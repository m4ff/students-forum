<%-- 
    Document   : group-posts
    Created on : Jan 26, 2014, 6:11:17 AM
    Author     : Pier DAgostino
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Student's Forum | ${groupName} </title>
        <%@include file="include/head.jsp" %>
    </head>
    <body>
        <c:forEach var="p" items="${posts}">
            <table id="table_id" dataTables_wrapper="sWrapper">
                <thead>
                    <tr>
                        <th>avatar</th>
                        <th>post</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><img style="max-width: 100%; height: auto" src="${p.getCreator().getAvatar()}"></td>
                        <td><c:out value="${p.getText()}"/></td>
                    </tr>
                </tbody>
            </table>
        </c:forEach>
        <a href="/post?id=${groupId}" data-role="button" data-inline="true" data-theme="b" type="submit">Add Post</a>
    </body>
</html>
