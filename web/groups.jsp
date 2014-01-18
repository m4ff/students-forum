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
        <title>Student's Forum | Groups</title>
        <%@include file="include/head.jsp" %>
    </head>
    <body>
        <div id="groups-page" data-role="page">
            <div data-role="header">
                <a href="#panel" data-icon="bars">Menu</a>
                <h2>Student's Forum | Groups</h2>
            </div>
            <div data-role="content">
                <ul data-role="listview" data-inset="true" data-split-icon="gear">
                    <li data-role="list-divider" data-theme="b">
                        Your groups
                    </li>
                    <c:forEach var="g" items="${groupList}">
                        <li>
                            <a href="">
                                <h2><c:out value="${g.getName()}"/></h2>
                                <span class="ui-li-count"><c:out value="${dbmanager.getPostsNumber(g)}"/></span>
                            </a>
                            <c:if test="${user != null && user.getId() == g.getCreator()}">
                                <a href="/group-manager?id=${g.getId()}">
                                </a>
                            </c:if>
                            <c:if test="${user != null && user.getId() != g.getCreator()}">
                                <a href="ciao" class="ui-state-disabled">
                                </a>
                            </c:if>
                        </li>
                    </c:forEach>
                </ul>
            </div>

            <%@include file="include/panel.jsp" %>
        </div>
    </body>
</html>
