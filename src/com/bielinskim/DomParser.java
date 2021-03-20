package com.bielinskim;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DomParser {

    Document doc;
    Map<Integer, Actor> actorsMap;

    DomParser() {
        try {
            File inputFile = new File("movie.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List parseActors() {
        NodeList actorNodes = doc.getElementsByTagName("aktor");
        actorsMap = new TreeMap<>();
        List actors = new ArrayList<Actor>();
        for (int i = 0; i < actorNodes.getLength(); i++) {
            Node nNode = actorNodes.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                int id = Integer.parseInt(eElement.getAttribute("id"));
                String firstName =  eElement
                        .getElementsByTagName("imie")
                        .item(0)
                        .getTextContent();
                String lastName = eElement
                        .getElementsByTagName("nazwisko")
                        .item(0)
                        .getTextContent();
                String gender = eElement
                        .getElementsByTagName("plec")
                        .item(0)
                        .getTextContent();
                String birthDate = eElement
                        .getElementsByTagName("data_urodzenia")
                        .item(0)
                        .getTextContent();
                Actor actor = new Actor(id, firstName, lastName, gender, birthDate);
                actors.add(actor);
                actorsMap.put(id, actor);
            }
        }
        return actors;
    }

    public List parseMovies() {
        NodeList movieNodes = doc.getElementsByTagName("film");
        List movies = new ArrayList<Movie>();
        for (int i = 0; i < movieNodes.getLength(); i++) {
            Node nNode = movieNodes.item(i);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                int id = Integer.parseInt(eElement.getAttribute("id"));
                String name =  eElement
                        .getElementsByTagName("nazwa")
                        .item(0)
                        .getTextContent();
                String releaseDate = eElement
                        .getElementsByTagName("rok_premiery")
                        .item(0)
                        .getTextContent();

                NodeList genreNodes = eElement.getElementsByTagName("gatunek");
                List genres = new ArrayList<String>();
                for (int j = 0; j < genreNodes.getLength(); j++) {
                    Node genreNode = genreNodes.item(j);
                    if (genreNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element genreElement = (Element) genreNode;
                        String genre = genreElement
                                .getTextContent();
                        genres.add(genre);
                    }
                }

                NodeList rolesNodes = eElement.getElementsByTagName("rola");
                List roles = new ArrayList<Role>();
                for (int j = 0; j < rolesNodes.getLength(); j++) {
                    Node roleNode = rolesNodes.item(j);
                    if (roleNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element roleElement = (Element) roleNode;
                        int actorId = Integer.parseInt(roleElement.getAttribute("idAktora"));
                        Actor actor = actorsMap.get(actorId);
                        String roleName = roleElement
                                .getTextContent();
                        roles.add(new Role(roleName, actor));
                    }
                }
                movies.add(new Movie(id, name, releaseDate, genres, roles));
            }
        }
        return movies;
    }


}
