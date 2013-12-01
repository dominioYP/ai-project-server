<%@page import="dati.Registration"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<% Registration registration = (Registration)request.getAttribute("registration");%>
</head>
<body>

<h3>Registration Successful</h3>

<table>
	<tr>
		<td>Username</td>
		<td><%= registration.getUserName() %></td>
	</tr>
	<tr>
		<td>Password</td>
		<td><%= registration.getPassword() %>
	</td>
	</tr>
	<tr>
		<td>Email</td>
		<td><%= registration.getEmail() %></td>
	</tr>
	
</table>

</body>
</html>