/*
** Copyright 2013 Software Composition Group, University of Bern. All rights reserved.
*/
package ch.unibe.scg.lexica;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lexica {

	private static final Logger logger = LoggerFactory.getLogger(Lexica.class);

	public static void main(String[] args) {
		try {
			Configuration.getInstance().parseArguments(args);
			IOperationMode mode = Configuration.getInstance().mode;
			mode.execute();
		} catch (IOException e) {
			logger.error("An error occured", e);
		}
	}

}
