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
        <title>Student's Forum | Login</title>
        <%@include file="include/head.jsp" %>
    </head>
    <body>
        <div id="login-page" data-role="page">
            <div data-role="header">
                <a href="/" data-icon="home">Home</a>
                <h2>Student's Forum | Login</h2>
            </div>
            <div data-role="content">
                <c:if test="${error != null}">
                    <div class="ui-body ui-body-b ui-corner-all">
                        ${error}
                    </div>
                </c:if>
                <form method="post" data-ajax="false">
                    <ul data-role="listview" data-inset="true">
                        <li data-role="fieldcontain">
                            <label for="email">Email</label>
                            <input type="text" id="email" name="email-field">
                        </li>
                    </ul>
                    <button data-inline="true" type="submit" data-theme="b">Send Email</button>
                </form>
            </div>
        </div>
    </body>
</html>
