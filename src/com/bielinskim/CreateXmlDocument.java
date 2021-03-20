package com.bielinskim;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import java.util.List;

public class CreateXmlDocument {

    Document doc;

    CreateXmlDocument() {
        try {
        DocumentBuilderFactory dbFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.newDocument();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    public void create(List<Actor> actors, List<Movie> movies) {
        try {

            Element movieLibrary = doc.createElement("filmoteka");
            doc.appendChild(movieLibrary);

            Element actorsElement = doc.createElement("aktorzy");
            movieLibrary.appendChild(actorsElement);

            Element moviesElement = doc.createElement("filmy");
            movieLibrary.appendChild(moviesElement);

            for(int i=0; i<actors.size(); i++) {
                Element actorElement = actors.get(i).getElement(doc);
                actorsElement.appendChild(actorElement);
            }

            for(int i=0; i<movies.size(); i++) {
                Element movieElement = movies.get(i).getElement(doc);
                moviesElement.appendChild(movieElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("movies_new.xml"));
            transformer.transform(source, result);

            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
