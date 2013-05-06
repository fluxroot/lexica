/*
** Copyright 2013 Software Composition Group, University of Bern. All rights reserved.
*/
package ch.unibe.scg.lexica;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Graph implements Closeable {

	private static final Logger logger = LoggerFactory.getLogger(Graph.class);
	
	private static final String FILENAME = ".lexica";

	private final Hashtable<String, Token> tokenTable = new Hashtable<>();
	private final Connection conn;
	
	public Graph(Path path) throws ClassNotFoundException, SQLException {
		Objects.requireNonNull(path);
		
		Path database = path.resolve(FILENAME);

		Class.forName("org.h2.Driver");
		conn = DriverManager.getConnection("jdbc:h2:" + database.toString());
	}
	
	@Override
	public void close() throws IOException {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.warn("Cannot close database connection", e);
			}
		}
	}

	public void newFile() {
		for (Token token : tokenTable.values()) {
			token.resetFileCount();
		}
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

	public void print() {
		for (Entry<String, Token> entry : tokenTable.entrySet()) {
			System.out.print(entry.getKey() + ";");
			System.out.format("%d;%.2f;%d%n", entry.getValue().getGlobalCount(), entry.getValue().getAverageFileCount(), entry.getValue().getFileCount());
		}
	}

}
