package model.haha;

import etu1906.framework.view.*;
import model.*;

public class Dept {
	static int operation;

    @Urls( url="dept-all" )
    public ModelView2 findAll(){
        ModelView2 view = new ModelView2( "list-dept.jsp" );
        view.addItem( "operation" , operation );
        return view;
    }     
    
   @Urls( url="verif-dept.do" )
    public ModelView2 verificationOperation(){
    	operation += 1;
        ModelView2 view = new ModelView2( "verifdept.jsp" );
        view.addItem( "operation" , operation );
        return view;
    }
}
