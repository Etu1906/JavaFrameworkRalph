<%@ page import = "model.haha.Emp"%>
<%
    String nom = (String) request.getAttribute("nom");
    String fichier = (String) request.getAttribute("fichier");
    out.print("bonjour"+nom+"fichier : "+fichier);
%>