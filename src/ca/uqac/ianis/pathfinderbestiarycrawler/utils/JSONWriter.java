package ca.uqac.ianis.pathfinderbestiarycrawler.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

import java.io.FileWriter;

public class JSONWriter {

    public static void saveToJSON(JSONObject jsonObject, String filename, boolean indentation){

        filename = filename.endsWith(".json") ? filename : filename+".json";

        // writing the JSONObject into a file
        try {
            String json;

            FileWriter fileWriter = new FileWriter(filename);
            if(indentation){
                ObjectMapper mapper = new ObjectMapper();
                Object jsonObjectNew = mapper.readValue(jsonObject.toJSONString(), Object.class);
                json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObjectNew);
            }
            else {
                json = jsonObject.toJSONString();
            }

            fileWriter.write(json);

            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
