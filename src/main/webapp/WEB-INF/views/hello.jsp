<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	<h3>Message : ${message}</h3>	
	<h3>Username : ${username}</h3>	
 
	<a href="${pageContext.request.contextPath}/j_spring_security_logout" > Logout</a>
 
</body>
</html>