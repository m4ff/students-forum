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
            <a href="/" data-ajax="false">
                Home
            </a>
        </li>
        <li>
            <a href="/groups"  data-ajax="false">
                My groups
            </a>
        </li>
        <c:if test="${user != null}">
        <li>
            <a href="/group-manager"  data-ajax="false">
                Create new group
            </a>
        </li>
        <li>
            <a href="/account"  data-ajax="false">
                My account
            </a>
        </li>
        </c:if>
        <c:if test="${isModerator == true}">
            <li>
                <a href="/moderation"  data-ajax="false">
                    Moderation
                </a>
            </li>
        </c:if>
        <c:if test="${user == null}">
            <li data-theme="b">
                <a href="/login"  data-ajax="false">
                    Log in
                </a>
            </li>
            <li data-theme="b">
                <a href="/register"  data-ajax="false">
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