package model.haha;


import etu1906.framework.file.FileUpload;
import java.util.List;
import java.util.Vector;
import java.sql.*;
import etu1906.framework.view.*;
import model.*;
import java.io.FileOutputStream;
import java.util.Arrays;

@Scope(name="singleton")
public class Emp {

    String nom;
    float idprenom;
    Date data;
    FileUpload fichier;
    int[] check;
    static int operation;
    
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

    public Emp(String nom) {
        this.nom = nom;
    }
    
	@Urls( url="verif-emp.do" )
    public ModelView verificationOperation(){
    	operation += 1;
        ModelView view = new ModelView( "verifemp.jsp" );
        view.addItem( "operation" , operation );
        return view;
    }

    @Urls( url="liste.do")
    public ModelView getList(){
        ModelView view = new ModelView( "liste.jsp" );
        return view;
    }
    
    

    @Urls( url="details.do" )
    @Argument(argument={"prenom","id"} )
    public ModelView getDetails( String prenom , Integer id ){
        ModelView view = new ModelView( "details.jsp" );
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
    public ModelView save(){
        ModelView view = new ModelView( "save.jsp" );
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
    public ModelView form(){
        ModelView view = new ModelView( "form.jsp" );
        return view;
    }

    @Urls( url="emp-all.do" )
    public ModelView findAll(){
        System.out.println(" bonjuour ");
        ModelView view = new ModelView( "list-emp.jsp" );
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
