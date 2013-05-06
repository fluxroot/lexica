/*
** Copyright 2013 Software Composition Group, University of Bern. All rights reserved.
*/
package ch.unibe.scg.lexica;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Graph {

	private static final Logger logger = LoggerFactory.getLogger(Graph.class);

	private final Path path;
	private final Hashtable<String, Token> tokenTable = new Hashtable<>();
	private final Hashtable<String, FileInfo> fileTable = new Hashtable<>();
	
	public Graph(Path path) {
		Objects.requireNonNull(path);
		
		this.path = path;
	}
	
	public void newFile() {
		for (Token token : tokenTable.values()) {
			token.resetFileCount();
		}
	}

	public void logFile(Path file) throws IOException {
		Objects.requireNonNull(file);
		
		FileInfo fileInfo = new FileInfo(Files.size(file), Files.getLastModifiedTime(file));
		fileTable.put(file.toString(), fileInfo);
	}

	public void put(String name, String prev) {
		Token token = tokenTable.get(name);
		if (token != null) {
			if (prev != null) {
				if (token.getPrevTokens().contains(prev)) {
					// Do nothing for now, we should increment the token count here
				} else {
					token.getPrevTokens().add(prev);
				}
			} else {
				// Do nothing
			}
		} else {
			token = new Token(name);
			tokenTable.put(token.getName(), token);
		}
		
		token.incrementGlobalCount();
		token.incrementFileCount();
	}

	public boolean exists(Path file) throws IOException {
		Objects.requireNonNull(file);
		
		FileInfo fileInfo = fileTable.get(file.toString());
		if (fileInfo != null
				&& Files.size(file) == fileInfo.getSize()
				&& Files.getLastModifiedTime(file).equals(fileInfo.getFileTime())) {
			return true;
		} else {
			return false;
		}
	}
	
	public void print() {
		for (Entry<String, Token> entry : tokenTable.entrySet()) {
			System.out.print(entry.getKey() + ";");
			System.out.format("%d;%.2f%n", entry.getValue().getGlobalCount(), entry.getValue().getFileCount());
		}
	}

}