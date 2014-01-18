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

                <ul data-role="listview" data-inset="true">
                    <li data-role="list-divider" data-theme="b">
                        News from your groups
                    </li>
                    <li>
                        <a href="">
                            <h2>The Elder Scrolls</h2>
                            <span class="ui-li-count">4</span>
                            <p>The Elder Scrolls is a series of action role-playing open world fantasy video games primarily developed by Bethesda Game Studios and published by Bethesda Softworks</p>
                        </a>
                    </li>
                </ul>
            </div>

            <%@include file="include/panel.jsp" %>
        </div>
    </body>
</html>
