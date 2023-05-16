package model.util;
import model.*;
import java.util.*;
import  etu1906.framework.*;
import java.lang.reflect.*;

public class Utilitaire {


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

    public static Object[] setValueParam( Map<String, String[]> parameterMap , Parameter[] parameters  ){
        Object[] new_parameter = new Object[ parameters.length ]; int i = 0; String value=  null;String name = "";
        for (Parameter parameter : parameters) {
            name = parameter.getName();
            System.out.println(name);
            Class<?> type = parameter.getType();
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

    public static  HashMap<String , Mapping> getAllMethodInClass( Class<?> clazz , HashMap<String , Mapping> MappingUrls ){
        java.lang.reflect.Method[] methods = clazz.getDeclaredMethods();
        for (java.lang.reflect.Method method : methods) {
            Urls annotation = method.getAnnotation(Urls.class);
            if (annotation != null) {
                Mapping mapping = new Mapping( clazz.getName() , method.getName()  );
                MappingUrls.put( annotation.url() , mapping );
            }
        }
        return MappingUrls;
    }

    public static  HashMap<String , Mapping> getAllMethod( Vector<Class<?>> listpackage , HashMap<String , Mapping> MappingUrls  ){
        for (Class<?> clazz : listpackage) {
            getAllMethodInClass( clazz , MappingUrls );   
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
