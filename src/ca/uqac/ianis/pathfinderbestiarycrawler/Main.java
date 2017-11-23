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
        //on récupère la page "index" répertoriant les liens vers toutes les créatures
        Document index = DocumentReader.fromUrl(pathfinderBaseUrl+bestiaryIndexUrl);

        //on sélectionne les nodes qui contiennent les liens vers les monstres
        Creature.creaturesLinks = index.body().select("[id=monster-index-wrapper]").select("[href*=.html]");

        //on crée un objet JSON vide pour stocker nos créatures
        JSONObject creatures = new JSONObject();

        //on parcours tous nos nodes (ou "liens") de créatures
        for(Element creatureLink : Creature.creaturesLinks){

            String creatureName = creatureLink.html();
            Creature creature = new Creature(creatureName);//nouvelle créature, juste avec son nom, sans ses sorts

            //on aura uniquement les éléments ayant class="stat-block-*" ainsi que les <h1>
            Elements paragraphNodes = DocumentReader
                                        .fromUrl(pathfinderBaseUrl+creatureLink.attr("href"))
                                        .body()
                                        .select("[class=body-content]")
                                        .select("[class=body]")
                                        .select("[class^=stat-block-],h1");

            String creatureAnchor;//on utilise l'ancre HTML pour gérer les pages avec plusieurs créatures
            if(creatureLink.outerHtml().contains("#")) {
                creatureAnchor = creatureLink.attr("href").split("#")[1];
            }
            else{
                creatureAnchor = creatureLink.attr("href").split(".html")[0];
            }

            //ces booléens permettront de suivre l'avancement dans les nodes
            boolean currentCreatureH1NodeSeen = false;
            boolean otherCreatureH1NodeSeen = false;
            boolean offenseNodeSeen = false;

            for (Element paragraphNode : paragraphNodes) {
                if (otherCreatureH1NodeSeen) {
                    System.out.println("#####################################"+creatureName);
                    break;//si on a vu le noeud d'une autre créature c'est qu'on a déjà passé les noeuds qui nous intéressent, on passe à
                }

                if (paragraphNode.outerHtml().contains("<h1") && paragraphNode.attr("id").equals(creatureAnchor)) {
                    if (currentCreatureH1NodeSeen) {
                        otherCreatureH1NodeSeen = true;
                    }

                    currentCreatureH1NodeSeen = true;
                }
                else {
                    if (!offenseNodeSeen) {
                        if (paragraphNode.attr("class").equals("stat-block-breaker") && paragraphNode.html().equals("offense")) {
                            offenseNodeSeen = true;
                        }
                    } else {
                        if (paragraphNode.attr("class").equals("stat-block-breaker") && paragraphNode.html().equals("statistics")) {
                            break;
                        }

                        Elements separatedSpells = paragraphNode.select("[href^=/pathfinderRPG/prd/coreRulebook/spells/]");
                        for (Element spell : separatedSpells) {
                            creature.addSpell(spell.html());
                        }
                    }
                }
            }

            creatures.put((int) Creature.creaturesCount, creature.toJSON()); //on ajoute notre monstre, un id incrémenté, et ses sorts à l'objet JSON
        }

        JSONWriter.saveToJSON(creatures, "creatures.json", true);
    }
}
