package ca.uqac.ianis.pathfinderbestiarycrawler.utils;

import java.io.IOException;

public class Console {

    public static void printProgress(long progress) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.print("[");
        for(int i=0; i<(int)progress; i++){
            System.out.print("=");
        }
        System.out.print(">");
        for(int i=0; i<100-(int)progress; i++){
            System.out.print(" ");
        }
        System.out.println("] ("+progress+"%)");
    }
}
