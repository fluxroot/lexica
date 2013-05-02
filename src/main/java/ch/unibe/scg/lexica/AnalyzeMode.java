/*
** Copyright 2013 Software Composition Group, University of Bern. All rights reserved.
*/
package ch.unibe.scg.lexica;

import java.nio.file.Path;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyzeMode implements IOperationMode {

	private static final Logger logger = LoggerFactory.getLogger(AnalyzeMode.class);

	private final Path path;

	public AnalyzeMode(Path path) {
		Objects.requireNonNull(path);
		
		this.path = path;
	}

	@Override
	public void execute() {
		logger.info("Analyzing " + path);
		
		// TODO
	}

}
