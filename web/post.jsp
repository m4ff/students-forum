<%-- 
    Document   : post
    Created on : Jan 25, 2014, 5:28:56 PM
    Author     : Pier DAgostino
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Student's Forum | Create Post</title>
        <%@include file="include/head.jsp" %>
    </head>
    <body>
        <div id="moderation-page" data-role="page">
            <div data-role="header">
                <a href="#" data-icon="arrow-l" data-rel="back">Back</a>
                <h2>Student's Forum | Create Post</h2>
            </div>
            <div data-role="content">
                <c:if test="${error != null}">
                    <div class="ui-body ui-body-b ui-corner-all">
                        ${error}
                    </div>
                </c:if>
                <form action="/post?id=${groupId}" data-ajax="false" method="post" enctype="multipart/form-data" onsubmit="if($('#post-text').val() == ''){alert('You forgot to write your text');return false;}else return true">
                    <ul data-role="listview" data-inset="true">
                        <li data-role="fieldcontain">
                            <label for="post-text">Post text:</label>
                            <textarea id="post-text" placeholder="Type you're post" name="text" style="position: relative"></textarea>
                        </li>
                        <li data-role="fieldcontain">
                            <label for="post-more-files-button">Add more files:</label>
                            <button type="button" id="post-more-files-button" data-inline="true" data-mini="true" onclick="duplicate('#file-li')">Add one more file</button>
                        </li>
                        <li data-role="fieldcontain" id="file-li">
                            <label for="post-file">File:</label>
                            <input type="file" id="post-file" name="file">
                        </li>
                    </ul>
                    <button data-inline="true" data-theme="b" type="submit">Post it</button>
                </form> <%@include file="include/panel.jsp" %>
            </div>
    </body>
</html>