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
                <h2>Student's Forum | Login</h2>
            </div>
            <div data-role="content">
                <c:if test="${error != null}">
                <c:out value="${error}"/>
                </c:if>
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
                        <li data-role="fieldcontain">
                            <label for="login-remember">Keep me logged in</label>
                            <input type="checkbox" id="login-remembre" name="remember" value="1">
                        </li>
                    </ul>
                    <button data-inline="true" type="submit">Log in</button>
                    <a href="/register">Register</a>
                    
                    <c:if test="${redirect != null}">
                    <input type="hidden" name="redirect" value="<c:out value="${redirect}"/>">
                    </c:if>
                </form>
            </div>
        </div>
    </body>
</html>
