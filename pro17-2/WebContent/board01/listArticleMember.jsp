<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	request.setCharacterEncoding("UTF-8");
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 게시글 갯수</title>
<style>
.no-uline {
	text-decoration: none;
}

.sel-page {
	text-decoration: none;
	color: red;
}

.cls1 {
	text-decoration: none;
}

.cls2 {
	font-size: 30px;
	text-align: center;
}
</style>
</head>
<body>
	<table align="center" border="1" width="50%">
		<tr height="10" align="center" bgcolor="lightgreen">
			<td>회원 ID</td>
			<td>개시글 갯수</td>
		</tr>
		<c:choose>
			<c:when test="${articleMember == null }">
				<tr align="center" height="10">
					<td>게시글 없음</td>
				</tr>
			</c:when>
			<c:when test="${articleMember != null }">
				<c:forEach var="article" items="${articleMember}">

					<tr align="center" height="10">
						<td>${article.id }</td>
						<td>${article.lists }</td>
					</tr>
				</c:forEach>
			</c:when>
		</c:choose>
	</table>
</body>
</html>