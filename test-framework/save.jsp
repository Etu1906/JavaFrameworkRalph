<%@ page import = "model.haha.Emp"%>
<%
    String nom = (String) request.getAttribute("nom");
    float prenom = (float) request.getAttribute("prenom");
    out.print("bonjour"+nom+" prenom :  "+prenom);
%>