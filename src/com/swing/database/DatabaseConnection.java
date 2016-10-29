package com.swing.database;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DatabaseConnection {

	private static final Logger log = LogManager.getLogger(DatabaseConnection.class);
	
	public Connection connectionDB() {
		Connection c = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager
					.getConnection("jdbc:postgresql://localhost:5433/mySwing",
							"postgres", "postgres");
			c.setAutoCommit(false);
		} catch (Exception e) {
			log.fatal("Error Trying to connect to DB.");
			JOptionPane.showMessageDialog(null,"The Database is not accessible, please contact IT.");
			e.printStackTrace();
			System.exit(0);
		}
		log.info("The connection with the database was established.");
		return c;

	}
}
