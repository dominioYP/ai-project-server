<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/themes/smoothness/jquery-ui.css" />
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=true&libraries=places"></script>
<script src="https://www.google.com/jsapi"
        type="text/javascript"></script>
<script language="Javascript" type="text/javascript">
    //<!
    google.load('search', '1');
    </script>
<title>Inserzione</title>
<style>
      html, body, #map-canvas {
        height:330px;
        width:330px;
        margin: 0px;
        padding: 0px;
      }
    </style>
</head>
<body>
<form:form id="insertionForm" method="post" action="inserzione" commandName="inserzioneForm" enctype="multipart/form-data">
	<% String error = (String) request.getAttribute("error");
	if(null != error ) {%>
		<div id="hibernate.error" class="errors"><%= error %></div>
	<%} %>
	<table>
	<tr>
	<td>Descrizione<font color="red"><form:errors path="descrizione"></form:errors></font></td>
	</tr>
	<tr>
	<td><form:input id="descrizione" path="descrizione"/></td>
	<td><img id="preview" src=""></td>
	</tr>
	<tr>
	<td>Codice a barre<font color="red"><form:errors path="codiceBarre"></form:errors></font></td>
	</tr>
	<tr>
	<td><form:input id="codiceBarre" path="codiceBarre"/></td>
	</tr>
	<tr>
	<td>Categoria<font color="red"><form:errors path="categoria"></form:errors></font>
	</td>
	</tr>
	<tr>
	<td><form:select id="categorie" path="categoria" items="${categorie}"/></td>
	</tr>
	<tr>
	<td>SottoCategoria<font color="red"><form:errors path="sottoCategoria"></form:errors></font>
	</td>
	</tr>
	<tr>
	<td><form:select id="sottoCategorie" path="sottoCategoria"/></td>
	</tr>
	<tr>
	<td>Prezzo<font color="red"><form:errors path="prezzo"></form:errors></font></td>
	</tr>
	<tr>
	<td><form:input path="prezzo"/></td>
	</tr>
	<tr>
	<td>Dettaglio<font color="red"><form:errors path="argomento[0]"></form:errors></font>
	</td>
	<td><div id="aggiungiArgomento">+</div></td>
	</tr>
	<tr>
	<td><form:select path="argomento[0]" items="${argomenti}"></form:select>
	</td>
	</tr>
	<tr>
	<td><font color="red"><form:errors path="arg_corpo[0]"></form:errors></font></td>
	</tr>
	<tr id="dettaglio">
	<td><form:input path="arg_corpo[0]"/></td>
	</tr>
	<tr>	
	<td>Data Acquisizione<font color="red"><form:errors path="dataInizio"></form:errors></font>
	</td>
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
	<tr>
	<td>Supermercato<font color="red"><form:errors path="supermercato"></form:errors></font></td>
	<td>Indirizzo<font color="red"><form:errors path="indirizzo"></form:errors></font></td>
	</tr>
	<tr>
	<td><form:input id="supermercato" path="supermercato"/></td>
	<td>
	<td><form:input id="indirizzo" path="indirizzo"/></td>
	
	<tr><td><div id="map-canvas"></div></td></tr>
	<tr>
	<td>
		<tr>
            <td>Browse File :</td>
            <td><form:input type="file" path="file" /></td>
        </tr>
		
	
	<tr>
	<td><input type="submit" value="Invia">
	</td>	
	</tr>
	</table>
</form:form>

<script type="text/javascript" src="resources/js/inserzione.js"></script>
</body>
</html>