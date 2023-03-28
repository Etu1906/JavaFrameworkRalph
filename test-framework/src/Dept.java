package model.haha;

import etu1906.framework.view.*;
import model.*;

public class Dept {

    @Urls( url="dept-all" )
    public ModelView findAll(){
        ModelView view = new ModelView( "list-dept.jsp" );
        return view;
    }     
}
