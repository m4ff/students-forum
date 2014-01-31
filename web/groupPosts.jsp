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
        <title>Student's Forum | ${groupName}</title>
        <%@include file="include/head.jsp" %>
    </head>
    <body>
        <div id="login-page" data-role="page">
            <div data-role="header">
                <a href="#panel" data-icon="bars">Menu</a>
                <h2>Student's Forum | ${groupName}</h2>
                <a href="/post?id=${groupId}" data-role="button" data-icon="plus" type="submit" data-iconpos="notext">Add Post</a>
            </div>
            <div data-role="content">
                <ul data-role="listview">
                    <c:forEach var="p" items="${posts}" varStatus="s">
                        <li style="padding-left: 6em; background-image: url(${p.getCreator().getAvatar()}); background-position: 0.5em 1em; background-size: 5em; background-repeat: no-repeat">
                            <div class="ui-corner-all ui-body ${s.index % 2 == 1 ? "ui-body-a" : "ui-body-b"}">
                                <p><b>${p.getCreator().getName()}</b> wrote:</p>
                                <p style="white-space: normal">
                                    ${p.getText()}
                                </p>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
                <%@include file="include/panel.jsp" %>
            </div>
    </body>
</html>
