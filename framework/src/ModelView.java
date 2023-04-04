package etu1906.framework.view;
import java.util.HashMap;

public class ModelView{
    private String view;
    private HashMap<String , Object> data = new HashMap<String , Object>();

    public ModelView(String view) {
        setView(view);
    }

    public ModelView() {
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public void addItem( String key , Object value ){
        data.put(key, value);
    }


    
}