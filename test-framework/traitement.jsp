<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>formulaire</title>
</head>
<body>
	<%
	if( session.getAttribute("Isconnected") != null ){
		int chien = (int) request.getAttribute("chien");
		String nom = (String) request.getAttribute("nom");
		String img = (String) request.getAttribute("img");
		out.print("bonjour Monsieur "+nom+" avec "+chien+" chien et l'image "+img+" vous etes connecte ");
		if( session.getAttribute("admin")  != null){
			out.print(" vous etes admin </br><a href='personneall.do' >voir liste personnes</a><a href='adminDeath.do?dog="+chien+"&nom="+nom+"&path="+img+"' >Se deconnecter de l'admin</a>");
			
		}	
	%>
	<a href="deconnecte.do" >Se deconnecter</a>
	<% } else {
		out.print( "pas connecte" );
	} %>
	
</body>
</html>
