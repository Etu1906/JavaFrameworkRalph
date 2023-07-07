package model.haha;

import etu1906.framework.file.FileUpload;
import java.util.List;
import java.util.Vector;
import java.util.HashMap;
import java.sql.*;
import etu1906.framework.view.*;
import model.*;
import model.session.Session_ctrl;
import java.io.FileOutputStream;
import java.util.Arrays;

@Scope(name="singleton")
public class Personne extends Session_ctrl {
    String nom;
    FileUpload fichier;
	String aucun;
	
	public Personne(){}
	
	public Personne( String nom , String aucun ){
		setNom(nom);
		setAucun( aucun );
	}
	
	public void setNom( String nom ){
		System.out.println(" niditra nom : "+nom  );
		this.nom = nom;
	}
	
	public String getNom(){
		return nom;
	}
	
	public void setAucun( String nom ){
		this.aucun = nom;
	}
	
	public String getAucun(){
		return aucun;
	}
	
	public FileUpload getFichier(){
		return fichier;
	}
	
    public void setFichier( FileUpload fichier ){
        this.fichier = fichier;
    }
    
	@Urls( url="personneall.do" )
	@Session
    @Json
	@Auth( profil="admin" )
    public Vector<Personne> All(){
		Vector<Personne> lp = new Vector<Personne>();
		lp.add( new Personne( "Jean 1" , "oui" ) );
		lp.add( new Personne( "Jean 2" , "non" ) );
		lp.add( new Personne( "Jean 3" , "oui" ) );
		lp.add( new Personne( "Jean 4" , "non" ) );
		return lp;
    }
    
	@Urls( url="deconnecte.do" )
    public ModelView2 deconnexion(){
    	ModelView2 view = new ModelView2( "traitement.jsp" );
		view.setInvalidate( true );
    	return view;
    }
    
	@Urls( url="adminDeath.do" )
    @Argument(argument={"dog" , "path"} )
    public ModelView2 deleteAdmin( int dog , String path ){
		ModelView2 view = new ModelView2( "traitement.jsp" );
		view.addItem( "chien" , dog );
		view.addItem( "nom" , getNom() );
		view.addItem( "img" , path );
		addSessionDestroy("admin");
		return view;
    }

	@Urls( url="trait.do" )
	@Session
    @Argument(argument={"nb-chien"} )
	public ModelView2 traitement( int dog ){
		ModelView2 view = new ModelView2( "traitement.jsp" );
		view.addItem( "chien" , dog );
		view.addItem( "nom" , getNom() );
		if( getNom().compareToIgnoreCase("admin") == 0 ){
			addSessionAttribute( "admin" , true );
		}
		addSessionAttribute( "Isconnected" , true );
		view.addItem( "img" , getFichier().getName() );
		System.out.print(" view :  "+view.getView());
		return view;	
	}

	@Urls( url="connexion.do" )
	public ModelView2 index(){
		ModelView2 view = new ModelView2( "connex.jsp" );
		return view;	
	}
}
