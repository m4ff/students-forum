<%-- 
    Document   : panel
    Created on : Jan 5, 2014, 8:20:57 PM
    Author     : paolo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div data-role="panel" id="panel" data-display="overlay">
    <ul data-role="listview">
        <c:if test="${user != null}">
            <li data-theme="b">
                Logged as <b><c:out value="${user.getName()}"/></b>
            </li>
        </c:if>
        <li>
            <a href="/">
                Home
            </a>
        </li>
        <li>
            <a href="/groups">
                My groups
            </a>
        </li>
        <li>
            <a href="/account">
                My account
            </a>
        </li>
        <c:if test="${isModerator == true}">
            <li>
                <a href="/moderation">
                    Moderation
                </a>
            </li>
        </c:if>
        <c:if test="${user == null}">
            <li data-theme="b">
                <a href="/login">
                    Log in
                </a>
            </li>
            <li data-theme="b">
                <a href="/register">
                    Register
                </a>
            </li>
        </c:if>
        <c:if test="${user != null}">
            <li data-theme="b">
                <a href="/logout" data-ajax="false">
                    Log out
                </a>
            </li>
        </c:if>
    </ul>
</div>