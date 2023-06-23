import java.lang.reflect.Field;

public class Test {
    public static void main(String[] args) throws Exception {
        Fille instanceFille = new Fille();
        Class<?> classeMere = Fille.class.getSuperclass();
        Field attribut = classeMere.getDeclaredField("nom");
        attribut.setAccessible(true);
        Object valeur = attribut.get(instanceFille);

        System.out.println("Valeur de l'attribut de la classe mère : " + valeur.toString());
    }
}

class Mere {
    private String nom = "Valeur ";
}

class Fille extends Mere {
    // ...
}

