package ca.uqac.ianis.pathfinderbestiarycrawler.utils;

import org.json.simple.JSONObject;

import java.io.FileWriter;

public class JSONWriter {

    public static void saveToJSON(JSONObject jsonObject, String filename){

        filename = filename.endsWith(".json") ? filename : filename+".json";

        // writing the JSONObject into a file
        try {
            FileWriter fileWriter = new FileWriter(filename);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
