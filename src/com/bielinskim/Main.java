package com.bielinskim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;


public class Main {

    private List<String> pagesToVisit = new LinkedList<String>();

    public static void main(String[] args) {
        new Main().start();
    }

    public void start() {
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);

        pagesToVisit.add("https://www.uph.edu.pl/");
        pagesToVisit.add("https://forbot.pl/blog/");

        String phrase = "praca przy";
        Search.search("phrase", pagesToVisit, 2, true, phrase);
        //Search.search("img", pagesToVisit, 2, true);
        //Search.search("ref", pagesToVisit, 2, true);

    }
}
