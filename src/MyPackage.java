package model.util;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;
public class MyPackage{
    String name;
    public MyPackage(String name){
        this.name=name;
    }    

    
    public Vector<Class<?>> getClasses(Vector<Class<?>> classes ,  String name ) throws ClassNotFoundException {
        String packageName= name;
        if (classes == null) {
            classes = new Vector<Class<?>>();
        }
        
        // Get a File object for the package
        File directory = null;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }
            String path = packageName.replace('.', '/');
            URL resource = classLoader.getResource(path);
            if (resource == null) {
                throw new ClassNotFoundException("No resource for " + path);
            }
            System.out.println( " ressource : "+resource.getFile() );
            directory = new File(resource.getFile());
        } catch (NullPointerException x) {
            x.printStackTrace();
            throw new ClassNotFoundException(packageName + " (" + directory + ") does not appear to be a valid package");
        }
        if (directory.exists()) {
            // Get the list of the files contained in the package
            String[] files = directory.list();
            for (int i = 0; i < files.length; i++) {
                // we are only interested in .class files
                File file = new File(directory.getAbsolutePath() + File.separator + files[i]);
                System.out.println( " path : "+file.getAbsolutePath() );
                if (file.isDirectory()) {
                     if( packageName.isEmpty() == true )
                        getClasses(classes, file.getName());
                     else 
                        getClasses(classes, packageName + "." + file.getName());
                } else {
                    if (files[i].endsWith(".class")) {
                        // removes the .class extension
                        if( packageName.isEmpty() == true ){
                            System.out.println( " file : "+files[i].substring(0, files[i].length() - 6)) ;
                            classes.add(Class.forName( files[i].substring(0, files[i].length() - 6)));
                        }else{
                        System.out.println( "files : "+files[i] );
                        classes.add(Class.forName(packageName + '.' + files[i].substring(0, files[i].length() - 6)));
                        }
                    }
                }
            }
        } else {
            throw new ClassNotFoundException(packageName + " does not appear to be a valid package");
        }
        return classes;
    }

    public void listAnnoted( Class clazz , Vector<Class<?>> l ){
        int i = 0;
        for( i = 0 ; i != l.size() ; i++ ){
            if( l.get(i).isAnnotationPresent( clazz ) ){
                System.out.println( " class : "+l.get(i).getSimpleName() );
            }
        }
    }
}