package etu1906.framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.servlet.*;
import javax.servlet.http.*;
import model.util.*; 

import etu1906.framework.Mapping;
import model.util.Utilitaire;
public class FrontServlet extends HttpServlet{

    protected void processRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException{
        try{
            PrintWriter out = res.getWriter();
            // String context = req.getServletContext().getRealPath("");
            String url = req.getRequestURL().toString();  
            out.println(url);
            String value = Utilitaire.getUrl( url );
        }catch( Exception e ){
            e.printStackTrace();
        } 
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException{
        processRequest(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException{
        processRequest(req, res);
    }
}
