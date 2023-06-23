import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        // Création du HashMap
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("nom", "John Doe");
        hashMap.put("age", 30);
        //hashMap.put("langages", {"Java", "Python", "JavaScript");

        // Création de l'objet Gson
        Gson gson = new Gson();

        // Conversion en JSON
        String jsonString = gson.toJson(hashMap);
        System.out.println(jsonString);
    }
}

