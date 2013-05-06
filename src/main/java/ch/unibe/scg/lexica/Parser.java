/*
** Copyright 2013 Software Composition Group, University of Bern. All rights reserved.
*/
package ch.unibe.scg.lexica;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.UnmappableCharacterException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parser {

	private static final Logger logger = LoggerFactory.getLogger(Parser.class);

	private final Graph graph;
	private final BufferedReader reader;
	
	public Parser(Graph graph, BufferedReader reader) {
		Objects.requireNonNull(graph);
		Objects.requireNonNull(reader);
		
		this.graph = graph;
		this.reader = reader;
	}
	
	public void parse() throws IOException {
		String prevName = null;
		String name = "";
		
		int i = 0;
		try {
			while ((i = reader.read()) != -1) {
				char c = (char) i;
				
				if (Character.toString(c).matches("[ \\r\\n\\t]")) {
					// Ignore white spaces
					prevName = add(name, prevName);
					name = "";
				} else if (Character.toString(c).matches("[\\\"\\'\\`\\^\\|\\~\\\\\\&\\$\\%\\#\\@\\.\\,\\;\\:\\!\\?\\+\\-\\*\\/\\=\\<\\>\\(\\)\\{\\}\\[\\]]")) {
					// Ignore delimiters
					prevName = add(name, prevName);
					name = "";
				} else {
					// Add character to token
					name += c;
				}
			}
			
			prevName = add(name, prevName);
		} catch (UnmappableCharacterException e) {
			logger.warn("An error occured", e);
		}
	}
	
	private String add(String name, String prev) {
		if (!name.isEmpty()) {
			graph.put(name, prev);
		}
		
		return prev;
	}

}
