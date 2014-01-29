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
        <title>Student's Forum | Post List</title>
        <%@include file="include/head.jsp" %>
    </head>
    <body>
        <c:forEach var="p" items="${posts}">
            <c:out value="${p.getText()}"/>
        </c:forEach>
        <a href="/post?id=${groupId}" data-role="button" data-inline="true" data-theme="b" type="submit">Add Post</a>
    </body>
</html>
