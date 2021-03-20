package com.bielinskim;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

public class Movie {

    int id;
    String name;
    String releaseDate;
    List<String> genres;
    List<Role> roles;

    public Movie(int id, String name, String releaseDate, List genres, List roles) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List getGenres() {
        return genres;
    }

    public void setGenres(List genres) {
        this.genres = genres;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Movie " +
                "id=" + id +
                ", name='" + name + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", genres=" + genres +
                ", actors=" + roles;
    }

    public Element getElement(Document doc) {
        Element movieElement = doc.createElement("film");

        Attr attr = doc.createAttribute("id");
        attr.setValue(String.valueOf(id));
        movieElement.setAttributeNode(attr);

        Element nameElement = doc.createElement("nazwa");
        nameElement.appendChild(doc.createTextNode(name));

        Element releaseDateElement = doc.createElement("rok_premiery");
        releaseDateElement.appendChild(doc.createTextNode(releaseDate));

        Element genresElement = doc.createElement("gatunki");
        for(int i=0; i<genres.size(); i++) {
            Element genreElement = doc.createElement("gatunek");
            genreElement.appendChild(doc.createTextNode(genres.get(i)));
            genresElement.appendChild(genreElement);
        }

        Element rolesElement = doc.createElement("obsada");
        for(int i=0; i<roles.size(); i++) {
            Element roleElement = doc.createElement("rola");

            Attr roleAttr = doc.createAttribute("idAktora");
            roleAttr.setValue(String.valueOf(roles.get(i).getActor().getId()));
            roleElement.setAttributeNode(roleAttr);

            roleElement.appendChild(doc.createTextNode(roles.get(i).roleName));
            rolesElement.appendChild(roleElement);

        }

        movieElement.appendChild(nameElement);
        movieElement.appendChild(releaseDateElement);
        movieElement.appendChild(genresElement);
        movieElement.appendChild(rolesElement);

        return movieElement;
    }
}
