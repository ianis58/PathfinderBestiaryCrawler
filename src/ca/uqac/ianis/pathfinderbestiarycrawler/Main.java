package ca.uqac.ianis.pathfinderbestiarycrawler;

import ca.uqac.ianis.pathfinderbestiarycrawler.utils.DocumentReader;
import jdk.nashorn.internal.runtime.JSONFunctions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
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


        for(Element monsterLink : monsterLinks){
            System.out.println(monsterLink.attr("title"));//nom du monstre
            Document monsterPage = DocumentReader.fromUrl(pathfinderBaseUrl+monsterLink.attr("href"));
            Elements sorts = monsterPage.body().select("[class=Bestiaire]").select("[class=BD]").select("[class=BDsorts]");
            for(Element sort : sorts){
                System.out.println(sort.html());
            }

            
        }

    }
}
