package model.haha;

import etu1906.framework.view.*;
import model.*;

public class Emp {

    @Urls( url="emp-all" )
    public ModelView findAll(){
        System.out.println(" bonjuour ");
        ModelView view = new ModelView( "list-emp.jsp" );
        return view;
    }     
}
