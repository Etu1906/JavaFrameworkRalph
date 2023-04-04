package model.haha;

import java.util.List;
import java.util.Vector;

import etu1906.framework.view.*;
import model.*;

public class Emp {

    String nom;

    public Emp(){

    }

    public Emp(String nom) {
        this.nom = nom;
    }

    public static Vector<Emp> listEmp(){
        Vector<Emp> list = new Vector<Emp>();

        Emp emp1 = new Emp("Jean");
        Emp emp2 = new Emp("Paul");
        Emp emp3 = new Emp("II");

        list.add(emp1);
        list.add(emp2);
        list.add(emp3);

        return list;
    }

    @Urls( url="emp-all" )
    public ModelView findAll(){
        System.out.println(" bonjuour ");
        ModelView view = new ModelView( "list-emp.jsp" );
        Vector<Emp> l = listEmp();
        view.addItem("lst", l);
        return view;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }     
}
