package  etu1906.framework;

public class Mapping{
    String className;
    String method;
	String profil = null;
    
    public Mapping(String className, String method) {
        this.className = className;
        this.method = method;
    }

   public Mapping(String className, String method , String profil) {
        this.className = className;
        this.method = method;
        this.profil = profil;
    }

    public Mapping() {
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setProfil( String profil ) {
		this.profil = profil;
    }
    
    public String getProfil(){
   		return profil;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }

    

}
