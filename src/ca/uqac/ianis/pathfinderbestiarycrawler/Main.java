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

            Elements bdSorts = monsterPage.body().select("[class=Bestiaire]").select("[class=BD]").select("[class=BDsorts]");
            Elements pouvoirsMagiquesSorts = monsterPage.body().select("[class=Bestiaire]").select("[class=BD]").select("[class=BDtexte]").select("div:contains(Pouvoirs magiques)");
            Elements sortsPreparesSorts = monsterPage.body().select("[class=Bestiaire]").select("[class=BD]").select("[class=BDtexte]").select("div:contains(Sorts préparés)");

            if(pouvoirsMagiquesSorts.size() > 0){
                bdSorts.add(pouvoirsMagiquesSorts.get(0).nextElementSibling());
                //System.out.println(pouvoirsMagiquesSorts.get(0).nextElementSibling().toString());
            }

            if(sortsPreparesSorts.size() > 0){
                bdSorts.add(sortsPreparesSorts.get(0).nextElementSibling());
            }


            String monsterSorts = "";

            for(Element bdSort : bdSorts){
                monsterSorts += bdSort.select("[class=pagelink]").html().replaceAll("(\r\n|\n)", ",").replace("'", " ").toLowerCase() + ",";
            }

            if(monsterSorts.endsWith(",")){
                monsterSorts = monsterSorts.substring(0, monsterSorts.length() - 1);
            }

            if(monsterSorts.endsWith(",")){//présent deux fois, c'est dégeulasse mais c'est voulu :)
                monsterSorts = monsterSorts.substring(0, monsterSorts.length() - 1);
            }

            monsterSorts = StringSanitizer.clean(monsterSorts);
            List<String> sorts = Arrays.asList(monsterSorts.split(","));

            Set<String> set = new HashSet<String>(sorts);
            sorts = new ArrayList<String>(set);

            monsterSorts = "[" + StringUtils.implode(",", sorts) + "]";

            monsters.put(monsterName, monsterSorts);
        }

        JSONWriter.saveToJSON(monsters, "monsters.json");

    }
}
