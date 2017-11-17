package ca.uqac.ianis.pathfinderbestiarycrawler.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DocumentReader {

    public static Document fromUrl(String urlString){
        URL url;
        String indexHtml = "";

        try {
            // get URL content
            url = new URL(urlString);
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                indexHtml += inputLine;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Jsoup.parse(indexHtml);
    }


}
