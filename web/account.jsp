<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Student's Forum | Account</title>
        <%@include file="include/head.jsp" %>
    </head>
    <body>
        <div id="account-page" data-role="page">
            <div data-role="header">
                <a href="#panel" data-icon="bars">Menu</a>
                <h2>Student's Forum | Account</h2>
            </div>
            <div data-role="content">
                <c:if test="${error != null}">
                    <div class="ui-body ui-body-b ui-corner-all">
                        ${error}
                    </div>
                </c:if>
                <c:if test="${error == null && success == true}">
                    <div class="ui-body ui-body-a  ui-corner-all">
                        Your account was updated
                    </div>
                </c:if>
                <form method="post" enctype="multipart/form-data" data-ajax="false" onsubmit="var b = $('#account-password').val() == $('#account-password-conf').val(); if(!b) alert('Password check failed'); return b;">
                    <ul data-role="listview" data-inset="true">
                        <li data-role="fieldcontain">
                            <img src="/avatar?id=${user.getId()}" style="height: 100%; width: auto">
                            <label for="account-avatar">Avatar</label>
                            <input type="file" id="account-avatar" name="avatar">
                        </li>
                        <li data-role="fieldcontain">
                            <label for="account-password">New password</label>
                            <input type="password" id="account-password" name="password">
                        </li>
                        <li data-role="fieldcontain">
                            <label for="account-password-conf">Confirm</label>
                            <input type="password" id="account-password-conf" name="password2">
                        </li>
                        <li data-role="fieldcontain">
                            <label for="account-old-password">Current password *</label>
                            <input type="password" id="account-old-password" name="current-password">
                        </li>
                    </ul>
                    <button data-inline="true" type="submit" data-theme="b">Update</button>
                    <span>
                        Fields marked with * are required. The avatar must be a JPEG file and must not exceed 1MB
                    </span>
                </form>
            </div>
            
            <%@include file="include/panel.jsp" %>
        </div>
    </body>
</html>
