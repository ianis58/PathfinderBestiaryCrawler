package ca.uqac.ianis.pathfinderbestiarycrawler.utils;

import java.util.List;

public class StringUtils {
    public static String implode(String glue, List<String> array) {
        boolean first = true;
        StringBuilder str = new StringBuilder();
        for (String s : array) {
            if (!first) str.append(glue);
            str.append(s);
            first = false;
        }
        return str.toString();
    }
}
