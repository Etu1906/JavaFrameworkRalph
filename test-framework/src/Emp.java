package model.haha;

import java.util.List;
import java.util.Vector;
import java.sql.*;
import etu1906.framework.view.*;
import model.*;

public class Emp {

    String nom;
    // float prenom;
    Date data;

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

    @Urls( url="save" )
    public ModelView save( String nom ){
        ModelView view = new ModelView( "save.jsp" );
        System.out.println(" nom :  "+getNom()); 
        // System.out.println(" prenom :  "+getPrenom()); 
        System.out.println(" data :  "+getData().toString());         

        view.addItem("nom",  getNom() );
        return view;
    }

    @Urls( url="form" )
    public ModelView form(){
        ModelView view = new ModelView( "form.jsp" );
        return view;
    }

    @Urls( url="emp-all" )
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

    // public float getPrenom() {
    //     return prenom;
    // }

    // public void setPrenom(float prenom) {
    //     this.prenom = prenom;
    // }
}
