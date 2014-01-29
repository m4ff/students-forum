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
        <title>Student's Forum | Register</title>
        <%@include file="include/head.jsp" %>
    </head>
    <body>
        <div id="register-page" data-role="page">
            <div data-role="header">
                <a href="/" data-icon="home">Home</a>
                <h2>Student's Forum | Register</h2>
            </div>
            <div data-role="content">
                <c:if test="${error != null}">
                <c:out value="${error}"/>
                </c:if>
                <form action="/register" method="post" enctype="multipart/form-data" data-ajax="false">
                    <ul data-role="listview" data-inset="true">
                        <li data-role="fieldcontain">
                            <label for="register-email">E-mail</label>
                            <input type="email" id="register-email" name="email">
                        </li>
                        <li data-role="fieldcontain">
                            <label for="register-username">User name</label>
                            <input type="text" id="register-username" name="username">
                        </li>
                        <li data-role="fieldcontain">
                            <label for="register-avatar">Avatar</label>
                            <input type="file" id="register-avatar" name="avatar">
                        </li>
                        <li data-role="fieldcontain">
                            <label for="register-password">Password</label>
                            <input type="password" id="register-password" name="password1">
                        </li>
                        <li data-role="fieldcontain">
                            <label for="register-password-conf">Confirm</label>
                            <input type="password" id="register-password-conf" name="password2">
                        </li>
                    </ul>
                    <button data-inline="true" type="submit" data-theme="b">Register</button>
                    <a href="/login">Login</a>
                </form>
            </div>
        </div>
    </body>
</html>
