package com.swing.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.NonNull;

public class DatabaseFunctions {

	Statement statement,statementAux;
	int id = 0;

	private static final Logger log = LogManager.getLogger(DatabaseFunctions.class);

	public boolean loginDB(@NonNull Connection c, String username, String password) {
		log.debug("Login actions to be preformed.");
		ResultSet rs = null;
		try {
			statement = c.createStatement();
			String sql = "select id from data.users where username='" + username + "' and password='" + password + "';";
			rs = statement.executeQuery(sql);
			if (!rs.next()) {
				return false;
			}
			else {
				return true;
			}
			
		} catch (SQLException e) {
			log.fatal("SQL Exception was caught while searching for the user information.");
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Error occured when processing the file.");
		}
		return true; 
	}
	
	public void insertDB(@NonNull Connection c, @NonNull String[] information) throws SQLException {
		try {
			id = getIdInsert(c);
			log.debug("The new line will be with id="+(id+1));
			statement = c.createStatement();
			String sql = "insert into data.info (id, name, surname, age, position, salary) VALUES ("+(id+1)+",'"
					+ information[1]+"','"+information[2]+"',"+information[3]+",'"+information[4]+"','"+information[5]+"');";
			statement.executeUpdate(sql);
			c.commit();
			log.info("The line " + information + " was inserted in the DB");

		} catch (SQLException e) {
			log.fatal("SQL Exception was caught while inserting information.");
			c.rollback();
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Error occured when processing the file.");
		} finally {
			if (c!=null && statement!=null) {
				statement.close();
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
			statement = c.createStatement();
			String sql = "delete from data.info where id="+id;
			statement.executeUpdate(sql);
			c.commit();
			log.info("The employee with id=" + id+ " was deleted.");

		} catch (SQLException e) {
			log.fatal("SQL Exception was caught while removing information.");
			c.rollback();
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Error occured when processing the file.");
		} finally {
			if (c!=null && statement!=null) {
				statement.close();
				c.close();
				log.info("The statement and connection were closed.");
			}
		}
	}

	public ResultSet showDbInfo(@NonNull Connection c) {
		ResultSet rs = null;
		try {
			log.debug("Getting all employees from DB.");
			statement = c.createStatement();
			rs = statement.executeQuery("SELECT id, name, surname, age, position, salary FROM data.info");
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
			statementAux = c.createStatement();
			ResultSet rs = statementAux.executeQuery("SELECT id FROM data.info ORDER BY id DESC LIMIT 1");
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
			statementAux = c.createStatement();
			String sql = "Select id from data.info "
					+ "where name='"+ information[1] +"' and surname='"
					+ information[2]+"';";
			ResultSet rs = statementAux.executeQuery(sql);
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

	public ResultSet findId(@NonNull Connection c) {
		log.debug("Getting the id to fill ComboBox");
		ResultSet rs = null;
		try {
			statement = c.createStatement();
			String sql = "Select id from data.info";
			rs = statement.executeQuery(sql);
		} catch (SQLException e) {
			log.fatal("SQL Exception while getting the id for removal.");
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Error occured when processing the file.");
		} 
		return rs;
	}

	public void removeById(@NonNull Connection c, @NonNull Object object) {
		log.debug("Getting the id to delete by ComboBox");
		try {
			statement = c.createStatement();
			String sql = "delete from data.info where id="+object.toString();
			statement.executeUpdate(sql);
			c.commit();
			log.info("The employee with id=" + object.toString() + " was deleted.");

		} catch (SQLException e) {
			log.fatal("SQL Exception while getting the id for removal.");
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Error occured when processing the file.");
		}
	}

	public void modifyDB(@NonNull Connection c,@NonNull String[] nextLine) {
		log.debug("Modify user in DB.");
		int counter=0;
		String[] headerModify = {"operation","id","name","surname","age","position","salary"};
		try {
			statement = c.createStatement();
			String sqlPart1 = "update data.info";
			String sqlPart2 = " SET ";
			String sqlPart3 = " where id="+nextLine[1];

			for (int i=2; i<nextLine.length; i++) {
				if (!nextLine[i].isEmpty()) {
					if (counter ==0) {
						sqlPart2=sqlPart2+headerModify[i]+"='"+nextLine[i]+"'";
						counter++;
					}
					else {
						sqlPart2=""+sqlPart2+", "+headerModify[i]+"='"+nextLine[i]+"'";
					}
				}
				else {
					continue;
				}
			}
			String sql = sqlPart1+sqlPart2+sqlPart3;
			statement.executeUpdate(sql);
			c.commit();
			log.info("The employee with id=" + nextLine[1] + " was modified.");

		} catch (SQLException e) {
			log.fatal("SQL Exception while modifying the use ron DB.");
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Error occured when processing the file.");
		}

	}
}
