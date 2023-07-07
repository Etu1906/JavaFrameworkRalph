<%@ page import = "model.haha.Emp"%>
<%@ page import = "java.util.Vector"%>
<% 
    Vector<Emp> list = (Vector<Emp>) request.getAttribute("lst");
%>
<h1>
    Welcome emp
</h1>

<%
    for( int i = 0 ; i != list.size() ; i++ ){
        out.print(  list.get(i).getNom()+"</br>" );
    }
%>
