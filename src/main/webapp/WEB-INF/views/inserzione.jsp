<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
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
	<td><form:errors path="*" cssClass="errorblock" element="div" />
 
		Carica una foto se non trovata : <input id="file" type="file" name="file" />
		<span><form:errors path="file" cssClass="error" />
		</span>
		</td></tr>
	<tr>
	<td><input type="submit" value="Invia">
	</td>	
	</tr>
	</table>
</form:form>

</body>
<script type="text/javascript" src="resources/js/inserzione.js"></script>
</html>