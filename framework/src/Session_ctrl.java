package model.session;
import model.*;
import java.util.*;
import  etu1906.framework.*;
import java.lang.reflect.*;

public class Session_ctrl{
	HashMap<String , Object> Session_value = new HashMap<String , Object>() ;
	Vector<String> Session_destroy = new Vector<String>() ;
	Vector<String> Session_valueAdd =  new Vector<String>();
	
	public void addSessionAttribute( String key , Object value ){
		Session_valueAdd.add( key );
		Session_value.put( key , value );
		System.out.println("  session value : ( "+key+" , "+Session_value.get(key)+" ) / "+Session_value);
		
	}
	
	public Object Sessionget( String key ){
		return Session_value.get( key );
	}
	
	public void addSessionDestroy( String key ){
		Session_destroy.add( key );
		Session_value.remove( key );
	}
	
}
