<%
    String nom = (String) request.getAttribute("prenom");
    out.print("bonjour Monsieur "+nom);
%>