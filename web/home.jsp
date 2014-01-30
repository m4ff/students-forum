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
        <title>Student's Forum | Home</title>
        <%@include file="include/head.jsp" %>
    </head>
    <body>
        <div id="login-page" data-role="page">
            <div data-role="header">
                <a href="#panel" data-icon="bars">Menu</a>
                <h2>Student's Forum | Home</h2>
            </div>
            <div data-role="content">
                <c:if test="${!invitingGroups.isEmpty()}">
                    <ul data-role="listview" data-inset="true" data-split-icon="gear">
                        <li data-role="list-divider" data-theme="b">
                            ${notificationsTitle}
                        </li>
                        <c:forEach var="g" items="${invitingGroups}">
                            <li>
                                <a href="">
                                    <h2>${g.getName()}</h2>
                                </a>
                                <a href="#-popup" data-rel="popup"></a>
                                <div data-role="popup" id="-popup" data-transition="pop">
                                    <div data-role="controlgroup" style="margin: 0px">
                                        <a href="/invites?id=${g.getId()}&accepted=1" data-role="button" data-ajax="false" data-icon="check">Accept</a>
                                        <a href="/invites?id=${g.getId()}&accepted=0" data-role="button" data-ajax="false" data-icon="delete">Decline</a>
                                    </div>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>
                <c:if test="${isLogged}">
                   <!-- You have last logged on ${loginTime} -->
                    <div data-role="listview" data-inset="true">
                        <c:if test="${postFromLastTime.isEmpty()}">
                            <li data-role="list-divider" data-theme="b">
                                No news from your groups
                            </li>
                        </c:if>      
                        <c:if test="${!postFromLastTime.isEmpty()}">
                            <li data-role="list-divider" data-theme="b">
                                News from your groups
                            </li>
                            <li data-theme="b" style="padding-top: 0px; padding-bottom: 0px">
                                <div data-role="collapsible-set" data-inset="false" style="margin: 0px">
                                    <div data-role="collapsible" data-theme="b">
                                        <h3 style="margin: 0px">${postFromLastTime[0].getGroup().getName()}</h3>
                                        <ul data-role="listview">
                                            <c:forEach var="p" items="${postFromLastTime}" varStatus="s">
                                                <c:if test="${s.index > 0 && (postFromLastTime[s.index-1].getGroup().getId() != postFromLastTime[s.index].getGroup().getId())}">
                                                </ul>
                                            </div>
                                            <div data-role="collapsible" data-theme="b">
                                                <h3 style="margin: 0px">${p.getGroup().getName()}</h3>
                                                <ul data-role="listview">
                                                </c:if>
                                                <li>
                                                    <h3>${p.getCreator().getName()}</h3>
                                                    <p>${p.getText()}</p>
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </div>
                                </div>
                            </li>
                        </c:if>
                    </div>
                </c:if>
                <%@include file="include/panel.jsp" %>
            </div>
    </body>
</html>
