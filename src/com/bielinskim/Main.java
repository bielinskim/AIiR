package com.bielinskim;

import java.util.List;

public class Main {

    public static void main(String[] args) {

	DomParser domParser = new DomParser();

	List actors = domParser.parseActors();
	List movies = domParser.parseMovies();

	Console.displayList(actors);
	Console.displayList(movies);


	CreateXmlDocument doc = new CreateXmlDocument();
	doc.create(actors, movies);


    }
}
