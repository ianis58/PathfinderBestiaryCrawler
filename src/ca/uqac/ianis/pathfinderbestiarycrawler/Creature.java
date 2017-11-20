package ca.uqac.ianis.pathfinderbestiarycrawler;

import ca.uqac.ianis.pathfinderbestiarycrawler.utils.Console;
import ca.uqac.ianis.pathfinderbestiarycrawler.utils.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class Creature {
    String name;
    List<String> spells;
    static double creaturesCount = 0;
    static double creaturesTotal = 1;
    static long progress = 0;

    public Creature(String name) {
        this.name = name;
        this.spells = new ArrayList<String>();
        creaturesCount++;

        progress = Math.round(Creature.creaturesCount/Creature.creaturesTotal * (double) 100);

        Console.printProgress(progress);
    }

    public void addSpell(String spell){
        spells.add(spell);

        Set<String> set = new HashSet<String>(spells);//on vire les doublons
        spells = new ArrayList<String>(set);//on vire les doublons
    }

    public int getSpellsCount(){
        return spells.size();
    }

    public JSONObject toJSON(){
        JSONObject creatureJson = new JSONObject();
        JSONArray spellsJson = new JSONArray();
        spellsJson.addAll(spells);

        creatureJson.put("name", name);
        creatureJson.put("spells", spellsJson);

        return creatureJson;
    }


}
