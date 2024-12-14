<%--
  Created by IntelliJ IDEA.
  User: vizuser
  Date: 12/15/2024
  Time: 12:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>File Upload Sample</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/upload" enctype="multipart/form-data" method="post">
    <p>
        <label>Select a file: </label>
        <input type="file" name="file"/>
    </p>
    <input type="submit" value="Upload"/>
</form>
<table>
    <thead>
    <tr>
        <td>Name</td>
        <td>Email</td>
    </tr>
    </thead>
    <tbody>
        <c:forEach items="${requestScope.users}" var="user">
            <tr>
                <td>${user.value}</td>
                <td>${user.email}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>
</body>
</html>
