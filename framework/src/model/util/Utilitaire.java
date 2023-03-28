package model.util;
import model.*;
import java.util.*;
import  etu1906.framework.*;
public class Utilitaire {

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
