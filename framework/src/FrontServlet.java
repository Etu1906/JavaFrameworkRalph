package etu1906.framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.servlet.*;
import javax.servlet.http.*;
import model.util.*; 
import java.lang.reflect.*;

import etu1906.framework.Mapping;
import etu1906.framework.view.ModelView;
import model.util.Utilitaire;
import model.util.StringCaster;

public class FrontServlet extends HttpServlet{
    HashMap<String , Mapping> MappingUrls = new HashMap<String  , Mapping>();
    Vector<Class<?>> listpackage;
    String base ;

    public void init() throws ServletException {
        try{
            //prendre l'url du projet
            base = this.getInitParameter("base_url");
            MyPackage p=new MyPackage();
            listpackage =  p.getClasses( null  , "" );
            // toutes les methodes annotees
            this.MappingUrls = Utilitaire.getAllMethod(listpackage, MappingUrls ) ; 
        }catch( Exception e ){
            e.printStackTrace();
        }
    }

    public Mapping getMethod( String url )throws Exception{
        String method = "";
        Mapping map = new Mapping();
        for (Class<?> clazz : listpackage) {
            method = Utilitaire.getMethod(clazz, url);
            if( method != null ){
                map.setClassName(clazz.getSimpleName());
                map.setMethod(method);
            }
        }
        return map;
    }

    public void setValue( Object object , HttpServletRequest req  )throws  ServletException,IOException, Exception{
        // les parametres de la requete
        Map<String, String[]> parameterMap = req.getParameterMap();
/// Donnees
        String method = "",
        paramName = "";
        Object result = null ;
        Field field;
/// boucle de chq parametre envoye
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            paramName = entry.getKey();
            String[] paramValues = entry.getValue();
            //prendre le setter
            method =  Utilitaire.getSetterName(paramName);
            
            try{
                field = object.getClass().getDeclaredField( paramName );
                // appeler la methode set + parametre
                result = Utilitaire.callMethodByName(object, method ,  field.getType()  , StringCaster.cast(paramValues[0])  );                    
            }catch( NoSuchMethodException | NoSuchFieldException se ){
                System.err.println(se);
            }
        }
    }

    public ModelView getModelView( String className , String MethodName , HttpServletRequest req  )throws  ServletException,IOException , Exception{
        //instanciation de la classe 
        Class<?> clazz = Class.forName(className);

        // prendre la méthode 
        Method Method = clazz.getDeclaredMethod(  MethodName );

        Parameter[] parameters = method.getParameters();

        Object instanceClazz  = clazz.newInstance();

        //appeler les setters
        setValue(instanceClazz, req);

        //invocation
        Object result = Method.invoke(instanceClazz);


        // cast en modelView
        if( result instanceof ModelView ){
            ModelView modelViewResult = (ModelView) result;
            return  modelViewResult;
        }
        throw new Exception(" erreur lors de l'instanciation de la ModelView ");
    }

    public void setAttribute( ModelView modelViewResult , HttpServletRequest req )throws ServletException,IOException,Exception{
        // iteration data
        if( modelViewResult.getData() != null ){
            for( Map.Entry<String , Object> entry : modelViewResult.getData().entrySet() ){
                System.out.println( "key :  "+entry.getKey()+" value : "+(entry.getValue()) );
                req.setAttribute( entry.getKey() , entry.getValue() );
            }
        }
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException,Exception{
        PrintWriter out = res.getWriter();
        try{
            String url = req.getRequestURL().toString();  
            out.println(url);

            String value = Utilitaire.getUrl( url , base );

            if(  MappingUrls.get(value) == null )   throw new Exception(" cette url est invalide ");

            String className = MappingUrls.get(value).getClassName();
            String MethodName = MappingUrls.get(value).getMethod();
            // instanciation de la modelView
            ModelView modelViewResult = getModelView( className , MethodName , req );

            // donner Attribut 
            setAttribute( modelViewResult , req );

            // System.out.println(" view :  "+modelViewResult.getView());

            RequestDispatcher dispat = req.getRequestDispatcher(modelViewResult.getView());

            dispat.forward(req,res);

        }catch( Exception e ){
            e.printStackTrace();
            throw e;
        } 
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException{
        PrintWriter out = res.getWriter();
        try{
            processRequest(req, res);
        }catch( Exception e ){
            out.println(e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException{
        PrintWriter out = res.getWriter();
        try{
            processRequest(req, res);
        }catch( Exception e ){
            out.println(e.getMessage());
        }
    }
}
