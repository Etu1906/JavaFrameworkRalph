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
public class Emp extends Session_ctrl {
    String nom;
    float idprenom;
    Date data;
    FileUpload fichier;
    int[] check;
    static int operation;
    HashMap<String , Object> session = new HashMap<String , Object>() ;
    
    public int[] getCheck(){
    	return check;
    }
    
    public void setCheck( int[] check ){
    	this.check = check;
    }


    public FileUpload getFichier(){
        return fichier;
    }
    public void setFichier( FileUpload fichier ){
        this.fichier = fichier;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Emp(){

    }
    
	public Emp( String nom , float idprenom ){
		setNom(nom);
		setIdprenom( idprenom );
    }

    public Emp(String nom) {
        this.nom = nom;
    }
    
    @Json
	@Urls( url="ObjectJson.do" )
    public Emp toJson(){
    	Emp employe = new Emp( "Steave" , 1 );
    	return employe;
    }
    
	@Urls( url="authentifResult.do" )
	@Session
	@Auth( profil="admin" )
    public ModelView2 AuthentifResult(){
        ModelView2 view = new ModelView2( "verifemp.jsp" );
        view.addItem( "operation" , session.get("huhu") );
        //view.addSessionAttribute( "Isconnected" , true );
        System.out.println( "huhu value : "+  session.get("huhu") );
        return view;
    }
    
	@Urls( url = "addsession.do" )
	@Session
    public ModelView2 addsess(){
    	System.out.println( "add session" );
		ModelView2 view = new ModelView2( "none.jsp" ) ;
		addSessionAttribute( "huhu" , 5 );
		addSessionAttribute( "haha" , 5 );
	 	return view;
    }
    
    	
	@Urls( url = "remove.do" )
	@Session
    public ModelView2 remove(){
    	System.out.println( "remove" );
		ModelView2 view = new ModelView2( "none.jsp" ) ;
		System.out.println("haha value : "+Sessionget("haha"));
		addSessionDestroy("haha");
	 	return view;
    }
    
	@Urls( url = "invalidate.do" )
	@Session
    public ModelView2 invalid(){
    	System.out.println( "innvalidate" );
		ModelView2 view = new ModelView2( "none.jsp" ) ;
		System.out.println("haha value : "+Sessionget("haha"));
		view.setInvalidate( true );
	 	return view;
    }
    
	@Urls( url = "verifySession.do" )
	@Session
    public ModelView2 verifySession(){
		ModelView2 view = new ModelView2( "none.jsp" ) ;
		System.out.println("huhu value verify session : "+Sessionget("huhu"));
	 	return view;
    }
    
    @Urls( url = "json.do" )
    public ModelView2 jsonRetrun(){
		ModelView2 view = new ModelView2( "none.jsp" ) ;
		view.setJSON( true );
    	view.addItem( "huhu" , new Emp() );
	 	view.addItem("point" , 5); 
	 	view.addItem("bool" , false);
	 	System.out.println( " value json :  "+view.getData() );
	 	return view;
    }
    
	@Urls( url="authentif.do" )
	@Session
    public ModelView2 AuthentifTest(){
        ModelView2 view = new ModelView2( "verifemp.jsp" ) ;
        view.addItem( "operation" , operation );
        //view.addSessionAttribute( "Isconnected" , true );
		addSessionAttribute( "huhu" , 5 );
		addSessionAttribute( "admin" , true );
		addSessionAttribute( "Isconnected" , true );
		System.out.println(" adresse :  "+this);
        return view;
    }
    
    @Urls( url="verif-emp.do" )
    public ModelView2 verificationOperation(){
    	operation += 1;
        ModelView2 view = new ModelView2( "verifemp.jsp" );
        view.addItem( "operation" , operation );
        return view;
    }

    @Urls( url="liste.do")
    public ModelView2 getList(){
        ModelView2 view = new ModelView2( "liste.jsp" );
        return view;
    }
    

    @Urls( url="details.do" )
    @Argument(argument={"prenom","id"} )
    public ModelView2 getDetails( String prenom , Integer id ){
        ModelView2 view = new ModelView2( "details.jsp" );
        view.addItem( "prenom" , prenom );
        System.out.println( " id :  "+id );
        return view;
    }

    public static Vector<Emp> prenommp(){
        Vector<Emp> list = new Vector<Emp>();

        Emp emp1 = new Emp("Jean");
        Emp emp2 = new Emp("Paul");
        Emp emp3 = new Emp("II");

        list.add(emp1);
        list.add(emp2);
        list.add(emp3);

        return list;
    }

    // sauvegarde de l'image
    public void saveByte( String nameFile , byte[] bytes )throws Exception{
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(nameFile);
            fos.write(bytes);
            System.out.println("Le fichier a été sauvegardé avec succès :");
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    @Urls( url="save.do" )
    public ModelView2 save(){
        ModelView2 view = new ModelView2( "save.jsp" );
        System.out.println(" nom :  "+getNom()); 
        System.out.println(" fichier : "+getFichier().toString());
        // System.out.println(" prenom :  "+getPrenom()); 
        // System.out.println(" data :  "+getData().toString()); 
        System.out.println(" check : "+Arrays.toString( check  ));
        view.addItem("fichier",  getFichier().getName() );
        try{
            saveByte( getFichier().getName() , getFichier().getBytes() );
        }catch( Exception e ){
            e.printStackTrace();
        }
        view.addItem("nom",  getNom() );
        return view;
    }

    @Urls( url="form.do" )
    public ModelView2 form(){
        ModelView2 view = new ModelView2( "form.jsp" );
        return view;
    }

    @Urls( url="emp-all.do" )
    public ModelView2 findAll(){
        System.out.println(" bonjuour ");
        ModelView2 view = new ModelView2( "list-emp.jsp" );
        Vector<Emp> l = prenommp();
        view.addItem("lst", l);
        return view;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        System.out.println(" entre  dans nom :  "+nom);
        this.nom = nom;
    }     

    public float getIdprenom() {
        return idprenom;
    }

    public void setIdprenom(float idprenom) {
        this.idprenom = idprenom;
    }

    // public float getPrenom() {
    //     return prenom;
    // }

    // public void setPrenom(float prenom) {
    //     this.prenom = prenom;
    // }
}
