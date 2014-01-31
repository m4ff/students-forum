<%-- 
    Document   : groupManager
    Created on : 14-gen-2014, 14.32.07
    Author     : halfblood
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Student's Forum | Group Manager</title>
        <%@include file="include/head.jsp" %>
    </head>
    <body>
        <div data-role="page" id="group-manager-page">
            <div data-role="header">
                <a href="#panel" data-icon="bars">Menu</a>
                <h2>Student's Forum | Group Manager</h2>
            </div>
            <div data-role="content">
                <form action="/group-manager" data-ajax="false" method="post">
                    <input type="hidden" name="id" value="${groupId}">

                    <ul data-role="listview" data-inset="true">
                        <li data-role="fieldcontain">
                            <label for="group-name" type="text">
                                Name
                            </label>
                            <input name="change_group_name" type="text" id="group-name" value="${nameString}">
                        </li>
                    </ul>

                    <label>
                        This group is public
                        <input type="checkbox" name="group-public" value="true" ${group.isPublic() ? "checked" : ""}>
                    </label>

                    <ul data-role="listview" data-inset="true">
                        <li data-role="list-divider">
                            Members
                        </li>
                        <c:forEach items="${visibleFollowinUsers}" var="userObject">
                            <li data-role="fieldcontain">
                                <fieldset data-role="controlgroup" data-type="horizontal" data-mini="true">
                                    <legend>${userObject.getName()}</legend>
                                    <label for="visible-${userObject.getId()}">
                                        Visible
                                    </label>
                                    <input id="visible-${userObject.getId()}" name="${userObject.getId()}" value="visible" type="radio" checked>

                                    <label for="invisible-${userObject.getId()}">
                                        Invisible
                                    </label>
                                    <input id="invisible-${userObject.getId()}" name="${userObject.getId()}" value="invisible" type="radio">
                                </fieldset>
                            </li>
                        </c:forEach>
                        <c:forEach items="${notVisibleFollowinUsers}" var="userObject">
                            <li data-role="fieldcontain">
                                <fieldset data-role="controlgroup" data-type="horizontal" data-mini="true">
                                    <legend>${userObject.getName()}</legend>
                                    <label for="invite-${userObject.getId()}">
                                        Invite
                                    </label>
                                    <label for="visible-${userObject.getId()}">
                                        Visible
                                    </label>
                                    <input id="visible-${userObject.getId()}" name="${userObject.getId()}" value="visible" type="radio">

                                    <label for="invisible-${userObject.getId()}">
                                        Invisible
                                    </label>
                                    <input id="invisible-${userObject.getId()}" name="${userObject.getId()}" value="invisible" type="radio" checked>
                                </fieldset>
                            </li>
                        </c:forEach>
                        <li data-role="list-divider">
                            Others
                        </li>
                        <c:forEach items="${otherUsers}" var="userObject">
                            <li data-role="fieldcontain">
                                <fieldset data-role="controlgroup" data-type="horizontal" data-mini="true">
                                    <legend>${userObject.getName()}</legend>
                                    <label for="invite-${userObject.getId()}">
                                        Invite
                                    </label>
                                    <input id="invite-${userObject.getId()}" name="${userObject.getId()}" value="member" type="checkbox">
                                </fieldset>
                            </li>
                        </c:forEach>
                    </ul>
                    <button type="submit" data-inline="true" data-theme="b">Submit</button>
                </form>
            </div>
            <%@include file="include/panel.jsp" %>
        </div>
    </body>
</html>
