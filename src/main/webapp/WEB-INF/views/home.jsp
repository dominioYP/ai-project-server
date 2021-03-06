<%@page import="hibernate.Prodotto"%>
<%@page import="hibernate.Profilo"%>
<%@page import="hibernate.Utente"%>
<%@page import="hibernate.Argomenti"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="dati.Dati" %>
<%@page import="java.util.Map" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
    	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
        <% 
        Dati dati = (Dati)request.getAttribute("dati");
        %>
    </head>
    <body>
    <% String error = (String) request.getAttribute("error");
	if(null != error ) {%>
		<div id="hibernate.error" class="errors"><%= error %></div>
	<%} %>
		<div id="utenti">
	        <h1>Hello World!</h1>
	        <p>This is the homepage!</p>
	        <% for(Map.Entry<Long,Prodotto> u : dati.getProdotti().entrySet()){ %>
	        <%= u.getValue().getCodiceBarre() %><br>
	        <%= u.getValue().getDescrizione()%><br>
	        <%= u.getValue().getIdProdotto() %><br>
	        <%= u.getValue().getSottocategoria() %><br>
	        
	        <br>
	        <%} %>
	        
	        <button value="register" name="register" onclick="
				path=window.location.pathname;
				path=path+'/register';
				
		        $.get( path, function( data ) {
	        $( '#render' ).html( data );
	      });"></button>
    	</div>
    	<div id="render">
    	</div>
        
    </body>
</html>
