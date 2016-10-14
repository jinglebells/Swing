package com.swing.tutorial;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.log4j.Logger;

public class DatabaseConnection {

	Logger log = Logger.getLogger(DatabaseConnection.class.getName());
	
	public Connection connectionDB() {
		Connection c = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager
					.getConnection("jdbc:postgresql://localhost:5432/mySwing",
							"postgres", "kamme61199A");
			c.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
		log.info("The connection with the database was established.");
		return c;

	}
}
