<%@page import="dati.InserzioneForm"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Inserzione Inserita</title>
</head>
<body>
<% InserzioneForm inserzione = (InserzioneForm)request.getAttribute("inserzione"); %>
<table>
	<tr>
		<td>Descrizione Prodotto</td>
		<td><%= inserzione.getDescrizione() %></td>
	</tr>
	<tr>
		<td>Prezzo</td>
		<td><%= inserzione.getPrezzo() %>
	</td>
	</tr>
	<tr>
		<td>Codice a barre</td>
		<td><%= inserzione.getCodiceBarre() %></td>
	</tr>
	<tr>
		<td>Data Inserzione</td>
		<td><%= inserzione.getDataInizio() %></td>
	</tr>
	
</table>


</body>
</html>