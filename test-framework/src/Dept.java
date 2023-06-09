package model.haha;

import etu1906.framework.view.*;
import model.*;

public class Dept {
	static int operation;

    @Urls( url="dept-all" )
    public ModelView findAll(){
        ModelView view = new ModelView( "list-dept.jsp" );
        view.addItem( "operation" , operation );
        return view;
    }     
    
   @Urls( url="verif-dept.do" )
    public ModelView verificationOperation(){
    	operation += 1;
        ModelView view = new ModelView( "verifdept.jsp" );
        view.addItem( "operation" , operation );
        return view;
    }
}
