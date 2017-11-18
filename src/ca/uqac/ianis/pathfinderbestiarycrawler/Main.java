package ca.uqac.ianis.pathfinderbestiarycrawler;

import ca.uqac.ianis.pathfinderbestiarycrawler.utils.*;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

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
            String monsterName = StringSanitizer.clean(monsterLink.attr("title"));

            monstersCount++;
            long progress = Math.round(monstersCount/monstersTotal * (double) 100);

            Console.printProgress(progress);

            Document monsterPage = DocumentReader.fromUrl(pathfinderBaseUrl+monsterLink.attr("href"));

            Elements allSorts = monsterPage.body().select("[class=Bestiaire]").select("[class=BD]").select("[class=BDsorts]");
            Elements magicSorts = monsterPage.body().select("[class=Bestiaire]").select("[class=BD]").select("[class=BDtexte]").select("div:contains(Pouvoirs magiques)");
            Elements preparedSorts = monsterPage.body().select("[class=Bestiaire]").select("[class=BD]").select("[class=BDtexte]").select("div:contains(Sorts préparés)");

            if(magicSorts.size() > 0){
                allSorts.add(magicSorts.get(0).nextElementSibling());
            }

            if(preparedSorts.size() > 0){
                allSorts.add(preparedSorts.get(0).nextElementSibling());
            }

            String monsterSorts = "";

            for(Element bdSort : allSorts){
                monsterSorts += StringSanitizer.clean(bdSort.select("[class=pagelink]").html()) + ",";
            }

            List<String> sorts = Arrays.asList(monsterSorts.split(","));//on vire les doublons 1/3
            Set<String> set = new HashSet<String>(sorts);//on vire les doublons 2/3
            sorts = new ArrayList<String>(set);//on vire les doublons 3/3

            monsterSorts = "[" + StringUtils.implode(",", sorts) + "]";//on refait un string depuis notre list de strings

            monsterSorts = monsterSorts.replace("[,", "[");
            monsterSorts = monsterSorts.replace(",]", "]");

            monsters.put(monsterName, monsterSorts);//on ajoute notre monstre et ses sorts à l'objet JSON
        }

        JSONWriter.saveToJSON(monsters, "monsters.json");

    }
}
