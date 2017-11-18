package ca.uqac.ianis.pathfinderbestiarycrawler;

import ca.uqac.ianis.pathfinderbestiarycrawler.utils.DocumentReader;
import ca.uqac.ianis.pathfinderbestiarycrawler.utils.JSONWriter;
import jdk.nashorn.internal.runtime.JSONFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Documented;
import java.net.URL;
import java.net.URLConnection;

public class Main {
    private static String pathfinderBaseUrl = "http://www.pathfinder-fr.org/Wiki/";
    private static String bestiaryIndexUrl = "Pathfinder-RPG.Monstres.ashx";

    public static void main(String[] args) {
        Document index = DocumentReader.fromUrl(pathfinderBaseUrl+bestiaryIndexUrl);
        Elements monsterLinks = index.body().select("[class=page espace-col]").select("[href^=Pathfinder-RPG.]").select("[href*=.ashx]");

        double monstersCount = 0;
        double monstersTotal = monsterLinks.size();

        JSONObject monsters = new JSONObject();

        for(Element monsterLink : monsterLinks){
            String monsterName = monsterLink.attr("title");

            monstersCount++;
            double progress = monstersCount/monstersTotal * (double) 100;

            System.out.println(Math.round(progress) + "%");

            Document monsterPage = DocumentReader.fromUrl(pathfinderBaseUrl+monsterLink.attr("href"));

            Elements bdSorts = monsterPage.body().select("[class=Bestiaire]").select("[class=BD]").select("[class=BDsorts]");
            Elements pouvoirsMagiquesSorts = monsterPage.body().select("[class=Bestiaire]").select("[class=BD]").select("div:contains(Pouvoirs magiques)");
            Elements sortsPreparesSorts = monsterPage.body().select("[class=Bestiaire]").select("[class=BD]").select("div:contains(Sorts préparés)");

            bdSorts.addAll(pouvoirsMagiquesSorts);
            bdSorts.addAll(sortsPreparesSorts);

            String monsterSorts = "[";

            for(Element bdSort : bdSorts){
                monsterSorts += bdSort.select("[class=pagelink]").html().toString().replaceAll("(\r\n|\n)", ",").replace("'", " ").toLowerCase() + ",";
            }

            if(monsterSorts.endsWith(",")){
                monsterSorts = monsterSorts.substring(0, monsterSorts.length() - 1);
            }

            if(monsterSorts.endsWith(",")){//présent deux fois, c'est dégeulasse mais c'est voulu :)
                monsterSorts = monsterSorts.substring(0, monsterSorts.length() - 1);
            }

            monsterSorts += "]";

            monsters.put(monsterName, monsterSorts);
        }

        JSONWriter.saveToJSON(monsters, "monsters.json");

    }
}
