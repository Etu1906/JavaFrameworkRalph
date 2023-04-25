<%@ page import = "model.haha.Emp"%>
<%
    String nom = (String) request.getAttribute("nom");
    out.print("bonjour"+nom);
%>