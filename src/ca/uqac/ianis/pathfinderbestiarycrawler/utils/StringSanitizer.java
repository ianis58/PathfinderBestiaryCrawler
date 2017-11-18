package ca.uqac.ianis.pathfinderbestiarycrawler.utils;

public class StringSanitizer {
    public static String clean(String str){
        str = str.replaceAll("(\r\n|\n)", ",");
        str = str.replace("é", "e");
        str = str.replace("è", "e");
        str = str.replace("ê", "e");
        str = str.replace("ë", "e");
        str = str.replace("à", "a");
        str = str.replace("â", "a");
        str = str.replace("ô", "o");
        str = str.replace("û", "u");
        str = str.replace("ç", "c");
        str = str.replace("'", " ");
        str = str.replace(",dd,", ",");
        str = str.replace(",dd", "");
        str = str.replace("dd,", "");
        str = str.replace("\\u2019", " ");
        str = str.replace("\u2019", " ");
        str = str.toLowerCase();

        return str;
    }
}
