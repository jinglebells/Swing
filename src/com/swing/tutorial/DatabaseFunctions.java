package com.swing.tutorial;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;
import org.apache.logging.log4j.Logger;

import lombok.NonNull;

import org.apache.logging.log4j.LogManager;

public class DatabaseFunctions {

	Statement stmt,stmt2;
	int id = 0;
	
	 private static final Logger log = LogManager.getLogger(DatabaseFunctions.class);

	public void insertDB(@NonNull Connection c, @NonNull String[] information) throws SQLException {
		try {
			id = getIdInsert(c);
			log.debug("The new line will be with id="+(id+1));
			stmt = c.createStatement();
			String sql = "insert into data.info (id, name, surname, age, position, salary) VALUES ("+(id+1)+",'"
					+ information[1]+"','"+information[2]+"',"+information[3]+",'"+information[4]+"','"+information[5]+"');";
			stmt.executeUpdate(sql);
			c.commit();
			log.info("The line " + information + " was inserted in the DB");

		} catch (SQLException e) {
			log.fatal("SQL Exception was caught while inserting information.");
			c.rollback();
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Error occured when processing the file.");
		} finally {
			if (c!=null && stmt!=null) {
				stmt.close();
				c.close();
				log.info("The statement and connection were closed.");
			}
		}
	}
	
	public void removeDB(@NonNull Connection c, @NonNull String[] information) throws SQLException {
		try {
			id = getIdDelete(c, information);
			log.debug("The employee with id="+id+" will be deleted");
			if (id == 0){
				JOptionPane.showMessageDialog(null,"Employee not exist in database.");
				return;
			}
			stmt = c.createStatement();
			String sql = "delete from data.info where id="+id;
			stmt.executeUpdate(sql);
	        c.commit();
	        log.info("The employee with id=" + id+ " was deleted.");
	        
		} catch (SQLException e) {
			log.fatal("SQL Exception was caught while removing information.");
			c.rollback();
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Error occured when processing the file.");
		} finally {
			if (c!=null && stmt!=null) {
				stmt.close();
				c.close();
				log.info("The statement and connection were closed.");
			}
		}
	}

	public ResultSet showDbInfo(@NonNull Connection c) {
		ResultSet rs = null;
		try {
			log.debug("Getting all employees from DB.");
			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT id FROM data.info;");
		} catch (SQLException e) {
			log.fatal("SQL Exception while retrieving all information from DB.");
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Error occurred when getting information from DB.");
		}
		return rs;
	}
	
	private int getIdInsert(@NonNull Connection c) {
		try {
			log.debug("Getting the id to insert.");
			stmt2 = c.createStatement();
			ResultSet rs = stmt2.executeQuery("SELECT id FROM data.info ORDER BY id DESC LIMIT 1");
			while (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (SQLException e) {
			log.fatal("SQL Exception while getting the last id.");
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Error occured when processing the file.");
		}
		return id;
	}
	
	private int getIdDelete(@NonNull Connection c, @NonNull String[] information) {
		try {
			log.debug("Getting the id to remove.");
			stmt2 = c.createStatement();
			String sql = "Select id from data.info "
					+ "where name='"+ information[1] +"' and surname='"
					+ information[2]+"';";
			ResultSet rs = stmt2.executeQuery(sql);
			while (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (SQLException e) {
			log.fatal("SQL Exception while getting the id for removal.");
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Error occured when processing the file.");
		}
		return id;
	}
}
