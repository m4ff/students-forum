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
                <c:if test="${!postFromLastTime.isEmpty()}">
                    <div data-role="collapsible-set" data-theme="c" data-content-theme="d">
                        <c:forEach var="p" items="${postFromLastTime}" varStatus="s">
                            <c:if test="${s.index == 0}">
                                <div data-role="collapsible">
                                    <h3>${p.getGroup().getName()}</h3>
                                    <p>${p.getText()}</p>
                                </c:if>
                                <c:if test="${postFromLastTime[s.index-1].getGroup() == p.getGroup()}"> 
                                    <p>I'm the collapsible set content for section 1.<p>
                                    </c:if>
                                    <c:if test="${postFromLastTime[s.index-1].getGroup() != postFromLastTime[s.index].getGroup()}">
                                </div>
                                <div data-role="collapsible">
                                    <h3>${p.getGroup().getName()}</h3>
                                    <p>${p.getText()}</p>
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
                <%@include file="include/panel.jsp" %>
            </div>
    </body>
</html>
