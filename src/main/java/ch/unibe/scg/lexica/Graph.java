/*
** Copyright 2013 Software Composition Group, University of Bern. All rights reserved.
*/
package ch.unibe.scg.lexica;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Graph implements Closeable {

	private static final Logger logger = LoggerFactory.getLogger(Graph.class);
	
	private static final String FILENAME = ".lexica";

	private final Connection conn;
	
	public Graph(Path path, boolean create) throws ClassNotFoundException, SQLException {
		Objects.requireNonNull(path);
		
		Path database = path.resolve(FILENAME);

		Class.forName("org.h2.Driver");
		conn = DriverManager.getConnection("jdbc:h2:" + database.toString(), "sa", "");
		
		if (create) {
			Statement stmt = conn.createStatement();
			stmt.execute("DROP ALL OBJECTS");
			
			stmt = conn.createStatement();
			stmt.execute("CREATE TABLE IF NOT EXISTS tokens (token VARCHAR NOT NULL UNIQUE, global INT NOT NULL, average REAL NOT NULL, coverage INT NOT NULL, current INT NOT NULL)");

			stmt = conn.createStatement();
			stmt.execute("CREATE TABLE IF NOT EXISTS nexts (token VARCHAR NOT NULL, next VARCHAR NOT NULL, CONSTRAINT nextskey PRIMARY KEY (token, next))");

			stmt = conn.createStatement();
			stmt.execute("CREATE TABLE IF NOT EXISTS prevs (token VARCHAR NOT NULL, prev VARCHAR NOT NULL, CONSTRAINT prevskey PRIMARY KEY (token, prev))");
		}
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

	public void newFile() throws SQLException {
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("UPDATE tokens SET coverage = coverage + 1 WHERE current > 0");

		stmt = conn.createStatement();
		stmt.executeUpdate("UPDATE tokens SET average = (average + current) / 2");

		stmt = conn.createStatement();
		stmt.executeUpdate("UPDATE tokens SET current = 0");
	}

	public void put(String name, String prev) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM tokens WHERE token = '" + name + "'");
		rs.next();
		if (rs.getInt(1) == 1) {
			stmt = conn.createStatement();
			stmt.executeUpdate("UPDATE tokens SET global = global + 1, current = current + 1 WHERE token = '" + name + "'");

			if (prev != null) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT COUNT(*) FROM prevs WHERE token = '" + name + "' AND prev = '" + prev + "'");
				rs.next();
				if (rs.getInt(1) == 0) {
					stmt = conn.createStatement();
					stmt.executeUpdate("INSERT INTO prevs VALUES('" + name + "', '" + prev + "')");
				}

				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT COUNT(*) FROM nexts WHERE token = '" + prev + "' AND next = '" + name + "'");
				rs.next();
				if (rs.getInt(1) == 0) {
					stmt = conn.createStatement();
					stmt.executeUpdate("INSERT INTO nexts VALUES('" + prev + "', '" + name + "')");
				}
			}
		} else {
			stmt = conn.createStatement();
			stmt.executeUpdate("INSERT INTO tokens VALUES('" + name + "', 1, 0, 0, 1)");
		}
	}

	public void print() throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM tokens");
		while (rs.next()) {
			String token = rs.getString("token");
			int global = rs.getInt("global");
			float average = rs.getFloat("average");
			int coverage = rs.getInt("coverage");
			
			System.out.print(token + ";");
			System.out.format("%d;%.2f;%d%n", global, average, coverage);
		}
	}

}
