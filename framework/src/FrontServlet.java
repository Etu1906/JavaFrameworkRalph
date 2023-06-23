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
import java.util.Enumeration;
import model.session.Session_ctrl;
import etu1906.framework.Mapping;
import etu1906.framework.view.ModelView2;
import model.util.Utilitaire;
import model.util.StringCaster;
import model.Session;
import com.google.gson.Gson;

public class FrontServlet extends HttpServlet{
    HashMap<String , Mapping> MappingUrls = new HashMap<String  , Mapping>();
    //class et ces attributs
    HashMap<Class<?> , Field[] > ListFields = new HashMap<Class<?> , Field[] >();
    //liste singleton
    HashMap<Class<?> , Object > Singletons = new HashMap <Class<?> , Object > ();
    Vector<Class<?>> listpackage;
    String base ;
    String connected;

    public void init() throws ServletException {
        try{
            //prendre l'url du projet
            base = this.getInitParameter("base_url");
            System.out.println(" base init "+base);
            //est connecte
            connected = this.getInitParameter("sessionName");
            MyPackage p=new MyPackage();
            listpackage =  p.getClasses( null  , "" );
            // toutes les methodes annotees
            this.MappingUrls = Utilitaire.getAllMethod(listpackage, MappingUrls , ListFields , Singletons ) ; 
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
                if( Utilitaire.estTableau( field.getType() ))
                	value = Utilitaire.creerTableau1D(  field.getType() , paramValues.length , paramValues ); 
                else
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
                System.out.println(" classe maintenant :  "+field.getType().getName());
         	 System.out.println(" value :  " + value );
                // appeler la methode set + parametre
                result = Utilitaire.callMethodByName(object, method ,  field.getType()  ,  value  );                    
            }catch( NoSuchMethodException | NoSuchFieldException se ){
                System.err.println(se);
            }
        }
    }
    
    
    // maka valeur anaty session
    public static HashMap<String , Object> getSessionAttributes( HttpServletRequest req ) {
        HttpSession session = req.getSession();
        HashMap<String, Object> attributes = new HashMap<>();
        Enumeration<String> attributeNames = session.getAttributeNames();
        
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = session.getAttribute(attributeName);
            attributes.put(attributeName, attributeValue);
        }
        
        return attributes;
    }
    
    public void addSessionAttribute( HttpSession httpsession , Object instanceClazz ){
		try{	
			Field session_field = Session_ctrl.class.getDeclaredField("Session_value");
			session_field.setAccessible(true);
			HashMap<String , Object> list_session = (HashMap<String , Object>) session_field.get( instanceClazz );
			System.out.println(" adresse 2 :  "+ session_field.get( instanceClazz )+" value ");
			//ajouter a la session
			addSession( list_session , httpsession );
		}catch( Exception e ){
			e.printStackTrace();
		}
    } 

    public ModelView2 getModelView2( String className , String MethodName , HttpServletRequest req , Map<String, String[]> parameterMap )throws  ServletException,IOException , Exception{
    	
        //instanciation de la classe 
        Class<?> clazz = Class.forName(className);
        HttpSession httpsession = req.getSession();

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
        
		// creation de l'objet tout en verifiant si singleton
        Object instanceClazz  = Utilitaire.Instanciation( clazz , Singletons , ListFields );
		
		if( Method.isAnnotationPresent(Session.class) ){
			System.out.println(" session present ");
	        HashMap<String , Object> session = getSessionAttributes( req );
	        Field field = instanceClazz.getClass().getDeclaredField("session");
        	field.setAccessible(true);
        	field.set(instanceClazz, session);
        	System.out.println("session values : "+field.get( instanceClazz ));
		}
		
		System.out.println("Object : "+instanceClazz);
		
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


		addSessionAttribute( httpsession , instanceClazz );
        // cast en ModelView2
        if( result instanceof ModelView2 ){
            ModelView2 ModelView2Result = (ModelView2) result;
            return  ModelView2Result;
        }
        throw new Exception(" erreur lors de l'instanciation de la ModelView2 ");
        
        
    }

    public void setAttribute( ModelView2 ModelView2Result , HttpServletRequest req )throws ServletException,IOException,Exception{
        // iteration data
        if( ModelView2Result.getData() != null ){
            for( Map.Entry<String , Object> entry : ModelView2Result.getData().entrySet() ){
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

	protected void verifySession( String profil , HttpSession session )throws Exception{
		//verification connexion
		if (session.getAttribute(connected) == null)
			throw new Exception(" vous n'etes pas connectee ");
		//verification role
		if( profil.compareToIgnoreCase("") != 0 ){
			if (session.getAttribute(profil) == null)
				throw new Exception("Authentification incorrecte");
		}
	}

	protected void verifyAuthentification( String profil , HttpSession session )throws Exception{
		if( profil != null )
			verifySession( profil , session );
	}
	
	protected void printSession( HttpSession session  ){
		System.out.println(" print session ");
		// Récupérez les noms des attributs de session
		Enumeration<String> attributeNames = session.getAttributeNames();

		// Parcourez les attributs de session et affichez leurs valeurs
		while (attributeNames.hasMoreElements()) {
			String attributeName = attributeNames.nextElement();
			Object attributeValue = session.getAttribute(attributeName);
			System.out.println("Attribute: " + attributeName + ", Value: " + attributeValue);
		}
	}
	
	//add session value
	protected void addSession( HashMap<String, Object> sessionData , HttpSession session )throws Exception{
		for (Map.Entry<String, Object> entry : sessionData.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			System.out.println(" attribute : session ( "+key+" , "+value+" ) ");
			session.setAttribute(key, value);
		}
		printSession( session );
	}

    protected void processRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException,Exception{
        System.out.println(" process request ");
        PrintWriter out = res.getWriter();
        HttpSession session = req.getSession();
        try{
            String url = req.getRequestURL().toString();
            System.out.println(" url : "+url+" et base :  "+base);
            //out.println(url);

            String value = Utilitaire.getUrl( url , base );

            if(  MappingUrls.get(value) == null )   throw new Exception(" cette url est invalide ");

            String className = MappingUrls.get(value).getClassName();
            String MethodName = MappingUrls.get(value).getMethod();
            String profil= MappingUrls.get(value).getProfil();
            
			//verification authentification
			verifyAuthentification( profil , session );

            Map<String, String[]> parameterMap = new HashMap<>();
            // Vérifiez si des fichiers ont été téléchargés
            if (isEnctypeForm( req )) {
                parameterMap = getParamForEnctypeForm( parameterMap , req );
            }else 
                // les parametres de la requete
                parameterMap = req.getParameterMap();

            // instanciation de la ModelView2
            ModelView2 ModelView2Result = getModelView2( className , MethodName , req , parameterMap);

			if( ModelView2Result.isJSON() == true ){
				 HashMap<String , Object> hashMap = ModelView2Result.getData(); 
				System.out.print( "nakato hhii  huhu: "+ModelView2Result.getData() );	
				String jsonString = Utilitaire.HashMapToJSON( ModelView2Result.getData() );
				System.out.print( jsonString );
				res.setContentType("application/json");
				res.setCharacterEncoding("UTF-8");
				out.print( jsonString );
					
			}else{

		        // donner Attribut 
		        setAttribute( ModelView2Result , req );

		        // System.out.println(" view :  "+ModelView2Result.getView());

		        RequestDispatcher dispat = req.getRequestDispatcher(ModelView2Result.getView());

		        dispat.forward(req,res);
	        }

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
