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
                    <table data-role="table" id="groups-table" class="ui-dbody-d table-stripe ui-responsive">
                        <thead>
                            <tr class="ui-bar-d">
                                <th><c:out value="${titleString}"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td> <input name="change_group_name" type="text" value="<c:out value="${nameString}"/>"> </td>
                            </tr>
                        </tbody>
                    </table>


                    <table data-role="table" id="user-table" class="ui-dbody-d table-stripe ui-responsive">
                        <thead>
                            <tr class=ui-bar-d>
                                <th>Name</th>
                                <th>Invite</th>
                                <th>Visible</th>
                                <th>Invisible</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${visibleFollowinUsers}" var="userObject"> 
                                <tr>
                                    <td><c:out value="${userObject.getName()}"/></td>
                                    <td> <input name="<c:out value="${userObject.getId()}"/>" value="member" type="checkbox" checked disabled> </td>
                                    <td> <input name="<c:out value="${userObject.getId()}"/>" value="visible" type="radio" checked> </td> 
                                    <td> <input name="<c:out value="${userObject.getId()}"/>" value="invisible" type="radio"> </td> 
                                </tr>
                            </c:forEach>
                            <c:forEach items="${notVisibleFollowinUsers}" var="userObject"> 
                                <tr>
                                    <td><c:out value="${userObject.getName()}"/></td>
                                    <td> <input name="<c:out value="${userObject.getId()}"/>" value="member" type="checkbox" checked disabled> </td> 
                                    <td> <input name="<c:out value="${userObject.getId()}"/>" value="visible" type="radio"> </td>
                                    <td> <input name="<c:out value="${userObject.getId()}"/>" value="invisible" type="radio" checked> </td>
                                </tr>
                            </c:forEach>
                            <c:forEach items="${otherUsers}" var="userObject"> 
                                <tr>
                                    <td><c:out value="${userObject.getName()}"/></td>
                                    <td> <input name="<c:out value="${userObject.getId()}"/>" value="member" type="checkbox"> </td>
                                    <td> <input name="<c:out value="${v.getId()}"/>" value="visible" type="radio"disabled> </td>
                                    <td> <input name="<c:out value="${userObject.getId()}"/>" value="invisible" type="radio" disabled> </td>
                                </tr>
                            </c:forEach>

                        </tbody>
                    </table>
                    <input type="submit" value="submit" data-inline="true">
                </form>
            </div>
            <%@include file="include/panel.jsp" %>
        </div>
    </body>
</html>
