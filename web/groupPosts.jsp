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
                <c:if test="${!isPublic && !isClosed}">
                    <a href="/post?id=${groupId}" data-role="button" data-icon="plus" type="submit">Add Post</a>
                </c:if>
            </div>
            <div data-role="content">
                <c:if test="${posts.size() != 0}">
                    <ul data-role="listview" data-inset="true">
                        <c:forEach var="p" items="${posts}" varStatus="s">
                            <li style="padding-left: 6em; background-image: url(${p.getCreator().getAvatar()}); background-position: 0.5em 1em; background-size: 5em; background-repeat: no-repeat">
                                <b>${p.getCreator().getName()}</b>
                                <div class="ui-corner-all ui-body ui-body-${s.index % 2 == 1 ? "a" : "a"}" style="background-color: #${s.index % 2 == 1 ? "8be88b" : "e8e88b"}">
                                    <p style="white-space: normal; font-size: 0.9em">
                                        ${p.getText(isPublic)}
                                    </p>
                                </div>
                                <ul style="font-size: 0.7em; padding-top: 0.7em">
                                    <c:forEach var="f" items="${dbmanager.getPostFiles(p)}">
                                        <li>
                                            <a target="_blank" href="${p.getGroup().getFilePath(f.key)}">${f.key} (${f.value.getSizeString()})</a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>
                <c:if test="${posts.size() == 0}">
                    <div class="ui-body ui-body-b ui-corner-all"><h3>The discussion thread is empty!</h3></div>
                </c:if>
                <%@include file="include/panel.jsp" %>
            </div>
    </body>
</html>
