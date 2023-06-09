package etu1906.framework.view;
import java.util.HashMap;

public class ModelView2{
    private String view;
    private HashMap<String , Object> data = new HashMap<String , Object>();
	private HashMap<String , Object> session = new HashMap<String , Object>();
	private boolean isJSON = false;
	private boolean invalidate = false;
    public ModelView2(String view) {
        setView(view);
    }
    
    public boolean isJSON(){
    	return isJSON;
    }
    
	public void setJSON( boolean json ){
		this.isJSON = json;
	}
		
    public ModelView2(String view , HashMap<String , Object> session) {
        setView(view);
       this.session = session;
    }

    public ModelView2() {
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public void addSessionAttribute( String key , Object value  ){
    	session.put( key , value );
    }

    public HashMap<String , Object> getSession(){
    	return session;
    }

    public HashMap<String, Object> getData() {
        return data;
    }
	public boolean isInvalidate(){
		return invalidate;
	}
	public void setInvalidate( boolean inv ){
		this.invalidate = inv;
	}
    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public void addItem( String key , Object value ){
        data.put(key, value);
    }


    
}
