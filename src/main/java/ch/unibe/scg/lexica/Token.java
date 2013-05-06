/*
** Copyright 2013 Software Composition Group, University of Bern. All rights reserved.
*/
package ch.unibe.scg.lexica;

import java.util.ArrayList;
import java.util.List;

public class Token {

	private final String name;
	
	private int globalCount = 0;
	private float averageFileCount = 0;
	private int currentFileCount = 0;
	
	private final List<String> prevTokens = new ArrayList<>();
	
	public Token(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getGlobalCount() {
		return globalCount;
	}
	
	public float getAverageFileCount() {
		return averageFileCount;
	}
	
	public List<String> getPrevTokens() {
		return prevTokens;
	}
	
	public void incrementGlobalCount() {
		++globalCount;
	}

	public void incrementFileCount() {
		++currentFileCount;
	}
	
	public void resetFileCount() {
		averageFileCount = (averageFileCount + currentFileCount) / 2;
		currentFileCount = 0;
	}

}
