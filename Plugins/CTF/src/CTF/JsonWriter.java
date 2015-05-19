package CTF;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Scyther on 05.05.2015.
 */
public class JsonWriter {

    @SuppressWarnings("unchecked")
    public static void Write(String[] args) throws IOException {

        JSONObject obj = new JSONObject();
        obj.put("Name", "crunchify.com");
        obj.put("Author", "App Shah");

        JSONArray company = new JSONArray();
        company.add("Compnay: eBay");
        company.add("Compnay: Paypal");
        company.add("Compnay: Google");
        obj.put("Company List", company);

        FileWriter file = new FileWriter("/Users/<username>/Documents/file1.txt");
        try {
            file.write(obj.toJSONString());
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + obj);

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            file.flush();
            file.close();
        }
    }


}
