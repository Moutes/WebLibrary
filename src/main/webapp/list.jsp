<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.library.managers.*" %>
<%@ page import="org.library.entity.*" %>

<%--
  Created by IntelliJ IDEA.
  User: ew
  Date: 20.4.2015
  Time: 16:28
  To change this template use File | Settings | File Templates.
--%>
<%!
%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<html>
<head>
    <title>Library</title>
</head>
<body>

<c:if test="${empty update}">
<form action="${pageContext.request.contextPath}/library/add" method="post">
  <h3>Add book:</h3>
  <table>
    <tr>
      <td>ISBN:</td>
      <td><input name="iso" value=""></td>
    </tr>
    <tr>
      <td>Edition:</td>
      <td><input name="edition" value=""></td>
    </tr>
  </table>
  <input type="submit" value="Add book"> <br>
</form>
</c:if>

<c:if test="${not empty update}">
  <form action="${pageContext.request.contextPath}/library/update" method="post">
    <h3>Update book:</h3>
    <table>
      <tr>
        <td>Id:</td>
        <td><input name="id" value="${update}"></td>
      </tr>
      <tr>
        <td>ISBN:</td>
        <td><input name="iso" value="${iso}"></td>
      </tr>
      <tr>
        <td>Edition:</td>
        <td><input name="edition" value="${edition}"></td>
      </tr>
    </table>
    <input type="submit" value="Update book"> <br>
  </form>
</c:if>

<c:if test="${not empty chyba}">
  <div style="border: solid 1px red; padding: 10px">
  <c:out value="${chyba}"/>
  </div>
</c:if>

<h1>Books</h1>
<table border="1">
  <thead>
  <tr>
    <th>
      ID
    </th>
    <th>
      ISBN
    </th>
    <th>
      Edition
    </th>
    <th>
      Edit
    </th>
  </tr>
  </thead>
<c:forEach items="${books}" var="item">
  <tr>
    <td>
      <c:out value="${item.idBook}"/>
    </td>
    <td>
      <c:out value="${item.iso}"/>
    </td>
    <td>
      <c:out value="${item.edition}"/>
    </td>
    <td>
      <form action="${pageContext.request.contextPath}/library/edit?id=${item.idBook}" method="post">
        <input type="submit" value="edit">
      </form>
      <form action="${pageContext.request.contextPath}/library/delete?id=${item.idBook}" method="post">
        <input type="submit" value="delete">
      </form>
    </td>
  </tr>
</c:forEach>
</table>
</body>
</html>
