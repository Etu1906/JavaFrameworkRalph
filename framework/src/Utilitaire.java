package model.util;
import model.*;
import java.util.*;
import  etu1906.framework.*;
import java.lang.reflect.*;
import com.google.gson.Gson;


public class Utilitaire {

	public static String  ToJSON( Object hashMap )throws Exception{
		try{
			Gson gson = new Gson();
			String jsonString = gson.toJson(hashMap);
			return jsonString;
		}catch( Exception e ){
			throw e;
		}
	}

	public static String  HashMapToJSON( HashMap<String , Object> hashMap )throws Exception{
		try{
			System.out.println(" key : ");
			Gson gson = new Gson();
			System.out.println(" key : ");
			String jsonString = gson.toJson(hashMap);
			return jsonString;
		}catch( Exception e ){
			throw e;
		}
	}


	public static Object getDefaultValue( Class<?> type ){
		System.out.println(" type field default : "+type);
		if( type == boolean.class )
			return false;
		if( type == byte.class )
			return (byte) 0;
		if (type == short.class)
			return (short) 0;
		if ( type == int.class )
			return 0;
		if ( type == float.class )
			return 0;
		if ( type == float.class )
			return 0;
		return null;
	}

	public static Object addDefaultValue( Object value , HashMap<Class<?> , Field[] > ListFields , Class<?> clazz )throws Exception{
		Field[] fields = ListFields.get( clazz );
		for( int i = 0 ; i != fields.length ; i++ ){
			fields[i].setAccessible(true);
			fields[i].set( value , getDefaultValue(fields[i].getType()) );
		}
		return value;
	}

	//verifier si la class doit etre un singleton
	public static Object Instanciation( Class<?> clazz , HashMap<Class<?> , Object > Singletons , HashMap<Class<?> , Field[] > ListFields )throws Exception{
		Object value = null;
		//si oui
		if( Singletons.containsKey( clazz ) ){
			//s'il y a deja une instanciation
			if( Singletons.get( clazz ) != null ){
				//mettre les valeurs par defaut des attributs
				value = Singletons.get( clazz ); 
				value = Utilitaire.addDefaultValue( value , ListFields , clazz );
				return value;
			}
			//1 ere instanciation
			value = clazz.newInstance();
			Singletons.put( clazz , value );
			return value;
		}
		//si non
		return clazz.newInstance();
	}

   public static void setValueIfPrimitiv( Object value , Class<?> type , String input ){
   	System.out.println( " classe prim :  "+type.getName()    );
   	if( type.getName().compareToIgnoreCase("int") == 0 ){
   			int parse = Integer.parseInt(input);
   			value = parse;
        }
   	if( type.getName().compareToIgnoreCase("float") == 0 ){
		float parse = Float.parseFloat(input);
		value = parse;
		return;
    }
       	if( type.getName().compareToIgnoreCase("boolean") == 0 ){
		boolean parse = Boolean.parseBoolean(input);
		value = parse;
		return;
    }
       	if( type.getName().compareToIgnoreCase("double") == 0 ){
		double parse = Double.parseDouble(input);
		value = parse;
		return;
    }
    value = StringCaster.cast( input , type );
    
   }

   public static Object[] creerTableau1D(Class<?> componentType, int taille, String[] valeur) throws Exception{
        Object[] tableau = (Object[]) Array.newInstance(componentType, taille);
        String paramValues = "";
        // Assigner la valeur à chaque élément du tableau
        for (int i = 0; i < taille; i++){
		paramValues = valeur[i];
		setValueIfPrimitiv( tableau[i] ,  componentType.getComponentType() , paramValues );
        }
         System.out.println(" tableau : "+tableau);
         int[][] tabl = (int[][]) tableau;
         System.out.println(" tabl :  "+tabl.length+"  " );
         System.out.println(tabl[0].length); 
        return tableau;
    }

   public static boolean estTableau(Class<?> clazz) {
	    return clazz.isArray();
   }



    public static Method  getMethod( String MethodName , Class clazz )throws Exception{
        Method[] methods =  clazz.getDeclaredMethods();
        for( Method met : methods ){
            if( met.getName().compareToIgnoreCase(MethodName) == 0 ){
                Class<?>[] parameterTypes = met.getParameterTypes();
                for (Class<?> parameterType : parameterTypes) {
                }
                return met;
            }
        }

        throw new Exception( " ne correspand a aucune fonction " );
    }

    // req.getParameter -> argument de la methode
    public static Object[] setValueParam( String[] args ,  Map<String, String[]> parameterMap ,Parameter[] parameters  ){
        Object[] new_parameter = new Object[ parameters.length ]; int i = 0; String value=  null;String name = "";String arg = "";
        for (Parameter parameter : parameters) {
            name = args[i];
            System.out.println(name);
            Class<?> type = parameter.getType();

            // if( parameters.getType() )
            if( parameterMap.get( name ) != null ) value = parameterMap.get( name )[0];
            else value = null;

            System.out.println( "nom : "+name+" value :  "+value );
            new_parameter[i++] = StringCaster.cast(value , type );
        }

        return new_parameter;
    }


    // req.getParameter -> argument de la methode
    public static Object[] setValueParam( Map<String, String[]> parameterMap , Parameter[] parameters  ){
        Object[] new_parameter = new Object[ parameters.length ]; int i = 0; String value=  null;String name = "";
        for (Parameter parameter : parameters) {
            name = parameter.getName();
            System.out.println(name);
            Class<?> type = parameter.getType();

            // if( parameters.getType() )
            if( parameterMap.get( name ) != null ) value = parameterMap.get( name )[0];
            else value = null;

            System.out.println( "nom : "+name+" value :  "+value );
            new_parameter[i++] = StringCaster.cast(value , type ); 
        }

        return new_parameter;
    }


    static String toUpperCaseFirstELement( String original )throws Exception{                                             
        //premier element en majuscule
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    /// pour setter
    public static String getSetterName( String value )throws Exception{
        //récupérer nom du setter               
        String set , method;
        set = "set";
        method = toUpperCaseFirstELement(value);
        return set + method;
      }

      public static <T> Object callMethodByName(Object object, String methodName ,  Class<T> parameterType,     Object parameterValue) throws NoSuchMethodException , Exception {
            try{
            	System.out.println( " fonction antsoina : "+methodName+" paramaetre : "+parameterType.getName() );
            	if( parameterValue != null ) 
            		System.out.println( "parametre : "+parameterValue.getClass().getName() );
                Method method = object.getClass().getMethod(methodName, parameterType);
                method.invoke(object, parameterValue);
                return object;
            }catch( NoSuchMethodException se ){
                throw se; 
            }
      }

    public static String getUrl(String url , String base ){
        int index =  url.indexOf(base); // trouver l'index de la chaîne "haha"
        System.out.println( base );
        System.out.println( url );
        if (index != -1) {
            String result = url.substring(index + base.length()); // extraire la sous-chaîne après "haha"
            System.out.println("result : "+result);
            return result;
        } else {
            return "none";
        }
    }

	public static String addAuthentification(Auth authentification ){
		if( authentification != null )
			return authentification.profil();
		return null;
	}

    public static  HashMap<String , Mapping> getAllMethodInClass( Class<?> clazz , HashMap<String , Mapping> MappingUrls , HashMap<Class<?> , Field[] > ListFields ){
        java.lang.reflect.Method[] methods = clazz.getDeclaredMethods();
        for (java.lang.reflect.Method method : methods) {
            Urls annotation = method.getAnnotation(Urls.class);
            //authentification par methode
            Auth authentification = method.getAnnotation(Auth.class);
            String auth = addAuthentification( authentification );
            if (annotation != null) {
            	ListFields.put( clazz , clazz.getDeclaredFields() );
                Mapping mapping = new Mapping( clazz.getName() , method.getName() , auth );
                MappingUrls.put( annotation.url() , mapping );
            }
        }
        return MappingUrls;
    }
    

    public static  HashMap<String , Mapping> getAllMethod( Vector<Class<?>> listpackage , HashMap<String , Mapping> MappingUrls , HashMap<Class<?> , Field[] > ListFields , HashMap<Class<?> , Object > Singletons  ){
        for (Class<?> clazz : listpackage) {
        	Scope singleton = clazz.getAnnotation(Scope.class);
        	if( singleton != null )
        		Singletons.put( clazz , null );
            getAllMethodInClass( clazz , MappingUrls , ListFields );   
        }
        return MappingUrls;
    }

    public static String getMethod( Class<?> clazz , String url ) {
        java.lang.reflect.Method[] methods = clazz.getDeclaredMethods();
        for (java.lang.reflect.Method method : methods) {
            System.out.println("method name :"+method.getName()+" compare to : "+url);
            Urls annotation = method.getAnnotation(Urls.class);
            if( annotation != null ){
                System.out.println("method annotation :"+annotation.url()+" compare to : "+url);
            }
            if (annotation != null && annotation.url().compareToIgnoreCase(url) == 0 ) {
                System.out.println("niditra");
                return method.getName();
            }
        }
        return null;
    }

}
