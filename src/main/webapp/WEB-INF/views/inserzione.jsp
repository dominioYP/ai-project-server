<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Inserzione</title>
</head>
<body>
<form:form method="post" action="register.html" commandName="registration">
	<% String error = (String) request.getAttribute("error");
	if(null != error ) {%>
		<div id="hibernate.error" class="errors"><%= error %></div>
	<%} %>
	<table>
	<tr>
	<td>Descrizione<font color="red"><form:errors path="descrizione"></form:errors></font></td>
	</tr>
	<tr>
	<td><form:input path="name"/></td>
	</tr>
	<tr>
	<td>Categoria<font color="red"><form:errors path="categoria"></form:errors></font>
	</td>
	</tr>
	<tr>
	<td><form:input path="categoria"/></td>
	</tr>
	<tr>
	<td>SottoCategoria<font color="red"><form:errors path="sottoCategoria"></form:errors></font>
	</td>
	</tr>
	<tr>
	<td><form:input path="sottoCategoria"/></td>
	</tr>
	<tr>
	<td>Prezzo<font color="red"><form:errors path="prezzo"></form:errors></font></td>
	</tr>
	<tr>
	<td><form:input path="prezzo"/></td>
	</tr>
	<tr>
	<td>Dettaglio<font color="red"><form:errors path="arg1"></form:errors></font></td>
	</tr>
	<tr>
	<td><form:select path="arg1" items="${argomenti}"></form:select>
	</td>
	</tr>
	<tr>
	<td><font color="red"><form:errors path="arg1_corpo"></form:errors></font></td>
	</tr>
	<tr>
	<td><form:input path="arg1_corpo"/></td>
	</tr>
	<tr>
	<td><input type="submit" value="Submit">
	</td>
	</tr>
	<tr>
	<td>Data Acquisizione<font color="red"><form:errors path="dataInizio"></form:errors></font></td>
	</tr>
	<tr>
	<td><form:input path="dataInizio"/></td>
	</tr>
	<tr>
	<td>Data Fine Promozione<font color="red"><form:errors path="dataFine"></form:errors></font></td>
	</tr>
	<tr>
	<td><form:input path="dataFine"/></td>
	</tr>
	</table>
</form:form>
</body>
</html>