<%-- 
    Document   : index
    Created on : Jan 5, 2014, 4:43:34 PM
    Author     : paolo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
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
                <h2>Student's Forum | Login</h2>
            </div>
            <div data-role="content">
                <form>
                    <ul data-role="listview" data-inset="true">
                        <li data-role="fieldcontain">
                            <label for="login-username">User name</label>
                            <input type="text" id="login-username" name="username">
                        </li>
                        <li data-role="fieldcontain">
                            <label for="login-password">Password</label>
                            <input type="password" id="login-password" name="password">
                        </li>
                    </ul>
                    <button data-inline="true" type="submit">Log in</button>
                    <a href="/register">Register</a>
                </form>
            </div>
        </div>
    </body>
</html>
