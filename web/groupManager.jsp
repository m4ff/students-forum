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
                            <label for="group-name" type="text"  value="<c:out value="${nameString}"/>">
                                <c:out value="${titleString}"/>
                            </label>
                            <input name="change_group_name" type="text" id="group-name" value="<c:out value="${nameString}"/>">
                        </li>
                    </ul>

                    <ul data-role="listview" data-inset="true">
                        <li data-role="list-divider">
                            Members
                        </li>
                        <c:forEach items="${visibleFollowinUsers}" var="userObject">
                            <li data-role="fieldcontain">
                                <fieldset data-role="controlgroup" data-type="horizontal">
                                    <legend><c:out value="${userObject.getName()}"/></legend>
                                    <%--<label for="invite-<c:out value="${userObject.getId()}"/>">
                                        Invite
                                    </label>
                                    <input id="invite-<c:out value="${userObject.getId()}"/>" name="<c:out value="${userObject.getId()}"/>" value="member" type="checkbox" checked disabled>
                                    --%>
                                    <label for="visible-<c:out value="${userObject.getId()}"/>">
                                        Visible
                                    </label>
                                    <input id="visible-<c:out value="${userObject.getId()}"/>" name="<c:out value="${userObject.getId()}"/>" value="visible" type="radio" checked>

                                    <label for="invisible-<c:out value="${userObject.getId()}"/>">
                                        Invisible
                                    </label>
                                    <input id="invisible-<c:out value="${userObject.getId()}"/>" name="<c:out value="${userObject.getId()}"/>" value="invisible" type="radio">
                                </fieldset>
                            </li>
                        </c:forEach>
                        <c:forEach items="${notVisibleFollowinUsers}" var="userObject">
                            <li data-role="fieldcontain">
                                <fieldset data-role="controlgroup" data-type="horizontal">
                                    <legend><c:out value="${userObject.getName()}"/></legend>
                                    <label for="invite-<c:out value="${userObject.getId()}"/>">
                                        Invite
                                    </label>
                                    <input id="invite-<c:out value="${userObject.getId()}"/>" name="<c:out value="${userObject.getId()}"/>" value="member" type="checkbox" checked disabled>

                                    <label for="visible-<c:out value="${userObject.getId()}"/>">
                                        Visible
                                    </label>
                                    <input id="visible-<c:out value="${userObject.getId()}"/>" name="<c:out value="${userObject.getId()}"/>" value="visible" type="radio">

                                    <label for="invisible-<c:out value="${userObject.getId()}"/>">
                                        Invisible
                                    </label>
                                    <input id="invisible-<c:out value="${userObject.getId()}"/>" name="<c:out value="${userObject.getId()}"/>" value="invisible" type="radio" checked>
                                </fieldset>
                            </li>
                        </c:forEach>
                        <li data-role="list-divider">
                            Others
                        </li>
                        <c:forEach items="${otherUsers}" var="userObject">
                            <li data-role="fieldcontain">
                                <fieldset data-role="controlgroup" data-type="horizontal">
                                    <legend><c:out value="${userObject.getName()}"/></legend>
                                    <label for="invite-<c:out value="${userObject.getId()}"/>">
                                        Invite
                                    </label>
                                    <input id="invite-<c:out value="${userObject.getId()}"/>" name="<c:out value="${userObject.getId()}"/>" value="member" type="checkbox">
                                    <%--
                                    <label for="visible-<c:out value="${userObject.getId()}"/>">
                                        Visible
                                    </label>
                                    <input id="visible-<c:out value="${userObject.getId()}"/>" name="<c:out value="${userObject.getId()}"/>" value="visible" type="radio" disabled>

                                    <label for="invisible-<c:out value="${userObject.getId()}"/>">
                                        Invisible
                                    </label>
                                    <input id="invisible-<c:out value="${userObject.getId()}"/>" name="<c:out value="${userObject.getId()}"/>" value="invisible" type="radio" disabled>
                                    --%>
                                </fieldset>
                            </li>
                        </c:forEach>
                    </ul>

                     <%--
                    <table data-role="table" id="user-table" class="ui-responsive">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Invite</th>
                                <th>Visible</th>
                                <th>Invisible</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${visibleFollowinUsers}" var="userObject"> 
                                <tr>
                                    <th>
                                        <c:out value="${userObject.getName()}"/>
                                    </th>
                                    <td>
                                        <label>
                                            Invite
                                            <input name="<c:out value="${userObject.getId()}"/>" value="member" type="checkbox" checked disabled>
                                        </label>
                                    </td>
                                    <td>
                                        <label>
                                            Visible
                                            <input name="<c:out value="${userObject.getId()}"/>" value="visible" type="radio" checked>
                                        </label>
                                    </td> 
                                    <td>
                                        <label>
                                            Invisible
                                            <input name="<c:out value="${userObject.getId()}"/>" value="invisible" type="radio">
                                        </label>
                                    </td> 
                                </tr>
                            </c:forEach>
                            <c:forEach items="${otherUsers}" var="userObject"> 
                                <tr>
                                    <th>
                                        <c:out value="${userObject.getName()}"/>
                                    </th>
                                    <td>
                                        <label>
                                            Invite
                                            <input name="<c:out value="${userObject.getId()}"/>" value="member" type="checkbox" checked disabled>
                                        </label>
                                    </td> 
                                    <td>
                                        <label>
                                            Visible
                                            <input name="<c:out value="${userObject.getId()}"/>" value="visible" type="radio">
                                        </label>
                                    </td>
                                    <td>
                                        <label>
                                            Invisible
                                            <input name="<c:out value="${userObject.getId()}"/>" value="invisible" type="radio" checked>
                                        </label>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:forEach items="${otherUsers}" var="userObject"> 
                                <tr>
                                    <th>
                                        <c:out value="${userObject.getName()}"/>
                                    </th>
                                    <td>
                                        <label style="display: inline">
                                            Invite
                                            <input name="<c:out value="${userObject.getId()}"/>" value="member" type="checkbox">
                                        </label>
                                    </td>
                                    <td>
                                        <label>
                                            Visible
                                            <input name="<c:out value="${v.getId()}"/>" value="visible" type="radio"disabled>
                                        </label>
                                    </td>
                                    <td>
                                        <label>
                                            Invisible
                                            <input name="<c:out value="${userObject.getId()}"/>" value="invisible" type="radio" disabled>
                                        </label>
                                    </td>
                                </tr>
                            </c:forEach>

                        </tbody>
                    </table>--%>
                    <button type="submit" data-inline="true" data-theme="b">Submit</button>
                </form>
            </div>
            <%@include file="include/panel.jsp" %>
        </div>
    </body>
</html>
