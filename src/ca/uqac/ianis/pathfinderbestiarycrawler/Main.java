package ca.uqac.ianis.pathfinderbestiarycrawler;

import ca.uqac.ianis.pathfinderbestiarycrawler.utils.*;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
    private static String pathfinderBaseUrl = "http://paizo.com/pathfinderRPG/prd/bestiary/";
    private static String bestiaryIndexUrl = "monsterIndex.html";

    public static void main(String[] args) {
        Document index = DocumentReader.fromUrl(pathfinderBaseUrl+bestiaryIndexUrl);
        Elements creaturesLinks = index.body().select("[id=monster-index-wrapper]").select("[href*=.html]");

        Creature.creaturesTotal = creaturesLinks.size();
        System.out.println(Creature.creaturesTotal);

        JSONObject creatures = new JSONObject();

        for(Element creatureLink : creaturesLinks){

            String creatureName = creatureLink.html();
            Creature creature = new Creature(creatureName);

System.out.println(creatureLink.outerHtml());
            Elements creatureBody = DocumentReader.fromUrl(pathfinderBaseUrl+creatureLink.attr("href")).body().getAllElements();


            Element currentNode;
            if(creatureLink.outerHtml().contains("#")) {
                String creatureAnchor = creatureLink.attr("href").split("#")[1];
                currentNode = creatureBody.select("[id="+creatureAnchor+"]").first();
            }
            else{
                currentNode = creatureBody.first().child(0);
            }

            boolean reachedStatBlockBreaker = false;
            boolean nodeEnded = false;

            do{
                currentNode = currentNode.nextElementSibling();

                if(currentNode != null) {
                    System.out.println(currentNode.toString());
                    if (currentNode.outerHtml().contains("stat-block-breaker")) {
                        if(creature.getSpellsCount() > 0){
                            reachedStatBlockBreaker = true;
                        }
                    }

                    Elements spellsElements = currentNode.select("[href^=/pathfinderRPG/prd/coreRulebook/spells/]");

                    for (Element newSpell : spellsElements) {
                        //System.out.println(newSpell.toString());
                        creature.addSpell(newSpell.html());
                    }
                }
                else {
                    nodeEnded = true;
                }

            }while (!reachedStatBlockBreaker && !nodeEnded);

            System.out.println(creature.toJSON().toJSONString());

            //break;
            //Elements allSorts = creaturePage.body().select("[class=Bestiaire]").select("[class=BD]").select("[class=BDsorts]");
            //Elements magicSorts = creaturePage.body().select("[class=Bestiaire]").select("[class=BD]").select("div:contains(Pouvoirs magiques \\(NLS )");
            //Elements preparedSorts = creaturePage.body().select("[class=Bestiaire]").select("[class=BD]").select("div:contains(Sorts préparés \\(NLS )");
/*
            if(magicSorts.size() > 1){
                allSorts.addAll(magicSorts);
                //System.out.println(creatureName +": "+ magicSorts.get(0).toString());
                allSorts.add(magicSorts.get(0));
            }

            if(preparedSorts.size() > 1){
                allSorts.addAll(preparedSorts);
                //System.out.println(creatureName +": "+ preparedSorts.get(0).toString());
                allSorts.add(preparedSorts.get(0).nextElementSibling());
            }
*/
            //String spellsTmp = "";
/*
            for(Element bdSort : allSorts){
                spellsTmp += StringSanitizer.clean(StringSanitizer.clean(bdSort.select("[class=pagelink]").html()))+",";
            }

            spellsTmp = StringSanitizer.clean(spellsTmp);

            for(String newSpell : spellsTmp.split(",")){
                creature.addSpell(newSpell);
            }

            creatures.put((int) Creature.creaturesCount, creature.toJSON());//on ajoute notre monstre et ses sorts à l'objet JSON

            JSONWriter.saveToJSON(creatures, "creatures.json");*/
        }

        JSONWriter.saveToJSON(creatures, "creatures.json");

    }
}
