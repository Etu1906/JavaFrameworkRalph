package etu1906.framework.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.Argument;
import model.util.*; 
import java.lang.reflect.*;
import java.util.Collection;

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

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] elements = contentDisposition.split(";");
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                return element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
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

    public void setValue( Object object , HttpServletRequest req  , Map<String, String[]> parameterMap )throws  ServletException,IOException, Exception{
/// Donnees
        System.out.println(" tonga eto setValue ");
        String method = "",
        paramName = "";
        Object result = null ;
        Field field;
/// boucle de chq parametre envoye
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            System.out.println(" les param : "+paramName);
            paramName = entry.getKey();
            String[] paramValues = entry.getValue();
            //prendre le setter
            method =  Utilitaire.getSetterName(paramName);
            
            try{
                System.out.println(" paramname :  "+paramName);
                field = object.getClass().getDeclaredField( paramName );
                System.out.println(" classe field :  "+field.getType().getName());
                Object  value = null;
                System.out.println(" est tableau :  "+Utilitaire.estTableau( field.getType()));
                //if( Utilitaire.estTableau( field.getType() ))
                //	value = Utilitaire.creerTableau1D(  field.getType() , paramValues.length , paramValues ); 
                //else
                	StringCaster.cast(paramValues[0] , field.getType()   );
                if( StringCaster.isFileUpload( field.getType() ) ){
                    System.out.println(" file upload yes ");
                    System.out.println(" value "+value);
                        
                    try{
                        value = StringCaster.castFileUpload(paramValues[0] , req.getPart( paramName ) );
                    }catch( Exception e ){
                        throw e;
                    }
                }
                // appeler la methode set + parametre
                result = Utilitaire.callMethodByName(object, method ,  field.getType()  ,  value  );                    
            }catch( NoSuchMethodException | NoSuchFieldException se ){
                System.err.println(se);
            }
        }
    }

    public ModelView getModelView( String className , String MethodName , HttpServletRequest req , Map<String, String[]> parameterMap )throws  ServletException,IOException , Exception{
        //instanciation de la classe 
        Class<?> clazz = Class.forName(className);

        // prendre la méthode 
        Method Method = Utilitaire.getMethod( MethodName , clazz );
        System.out.println(" nahita methode ");

        Parameter[] parameters = Method.getParameters();

        Object[] valueParameter = Utilitaire.setValueParam( parameterMap , parameters );

        if( Method.isAnnotationPresent(Argument.class) ){
            Argument arg = Method.getAnnotation( Argument.class );
            String[] arguments = arg.argument();
            valueParameter = Utilitaire.setValueParam(  arguments , parameterMap , parameters );
        }

        Object instanceClazz  = clazz.newInstance();

        //appeler les setters
        setValue(instanceClazz, req , parameterMap);

        String name = "";int i = 0;
        for (Parameter parameter : parameters) {
            name = parameter.getName();
            Class<?> type = parameter.getType();
            System.out.println(" methode  =  nom : "+name+" type :  "+type.getName()+" value: "+valueParameter[i++]);
        }

        //invocation
        Object result = Method.invoke(instanceClazz ,valueParameter );

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

    // verifier si le formulaire contient des fichiers telecharges
    private boolean isEnctypeForm( HttpServletRequest req ){
        if (req.getContentType() != null && req.getContentType().startsWith("multipart/form-data")) 
            return true;
        return false;
    }

    // remplir DataForm si formulaire avec des fichiers telecharges
    private Map<String, String[]> getParamForEnctypeForm( Map<String, String[]> parameterMap , HttpServletRequest req )throws Exception{
        // Parcourez toutes les parties de la requête
        Collection<Part> parts = req.getParts();
        for (Part part : parts) {
            if (part.getContentType() == null) {
                // C'est un champ de formulaire normal (non fichier)
                String fieldName = part.getName();
                String fieldValue = req.getParameter(fieldName);
                // Faites quelque chose avec le nom du champ (fieldName) et sa valeur (fieldValue)
                parameterMap.put(  fieldName , new String[]{ fieldValue } );
            } else {
                // C'est un fichier téléchargé
                String fileName = getFileName(part);
                InputStream fileContent = part.getInputStream();                        // Faites quelque chose avec le nom du fichier (fileName)
                parameterMap.put(  part.getName() , new String[]{getFileName(part)} );
            }
        }
        return parameterMap;
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException,Exception{
        System.out.println(" process request ");
        PrintWriter out = res.getWriter();
        try{
            String url = req.getRequestURL().toString();  
            out.println(url);

            String value = Utilitaire.getUrl( url , base );

            if(  MappingUrls.get(value) == null )   throw new Exception(" cette url est invalide ");

            String className = MappingUrls.get(value).getClassName();
            String MethodName = MappingUrls.get(value).getMethod();

            Map<String, String[]> parameterMap = new HashMap<>();
            // Vérifiez si des fichiers ont été téléchargés
            if (isEnctypeForm( req )) {
                parameterMap = getParamForEnctypeForm( parameterMap , req );
            }else 
                // les parametres de la requete
                parameterMap = req.getParameterMap();

            // instanciation de la modelView
            ModelView modelViewResult = getModelView( className , MethodName , req , parameterMap);

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
