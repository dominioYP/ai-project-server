<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register</title>
</head>
<body>
<form:form method="post" action="register.html" commandName="registration">
	<% String error = (String) request.getAttribute("error");
	if(null != error ) {%>
		<div id="hibernate.error" class="errors"><%= error %></div>
	<%} %>
	<table>
	<tr>
	<td>Username<font color="red"><form:errors path="userName"></form:errors></font></td>
	</tr>
	<tr>
	<td><form:input path="userName"/></td>
	</tr>
	<tr>
	<td>Password<font color="red"><form:errors path="password"></form:errors></font>
	</td>
	</tr>
	<tr>
	<td><form:password path="password"/></td>
	</tr>
	<tr>
	<td>Confirm Password<font color="red"><form:errors path="confirmPassword"></form:errors></font>
	</td>
	</tr>
	<tr>
	<td><form:password path="confirmPassword"/></td>
	</tr>
	<tr>
	<td>Email<font color="red"><form:errors path="email"></form:errors></font></td>
	</tr>
	<tr>
	<td><form:input path="email"/></td>
	</tr>
	<tr>
	<td><input type="submit" value="Submit">
	</td>
	</tr>
	
	</table>
</form:form>

</body>
</html>