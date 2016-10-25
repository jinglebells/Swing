package com.swing.gui;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opencsv.CSVReader;
import com.swing.database.DatabaseConnection;
import com.swing.database.DatabaseFunctions;

import lombok.NonNull;

public class AuxFunctions {

	private static final Logger log = LogManager.getLogger(AuxFunctions.class);

	DatabaseConnection dbConnection = new DatabaseConnection();
	DatabaseFunctions dbFunction = new DatabaseFunctions();

	Connection dbConnector;

	public void login(@NonNull String username, @NonNull String password) {
		
		try {
			dbConnector = dbConnection.connectionDB();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			String md5 = new BigInteger(1, md.digest()).toString(16); // Hash value
			if (dbFunction.loginDB(dbConnector, username, md5)) {
				UploadFrame uploadFrame = new UploadFrame();
				uploadFrame.setEnabled(true);
				uploadFrame.setVisible(true);
				LoginFrame.closeFrame(LoginFrame.getFrames());
				
			}
			else {
				JOptionPane.showMessageDialog(null,"Invalid Credentials");
				LoginFrame.username.setText("");
				LoginFrame.pwdPassword.setText("");
			}
		} catch (NoSuchAlgorithmException e) {
			log.fatal("MD5 Error");
			JOptionPane.showMessageDialog(null,"Problem found during login");
			e.printStackTrace();
		}
		dbFunction.loginDB(dbConnector, username, password);
	}
	
	public void parseCSVFile(@NonNull String path, @NonNull char separator, @NonNull char quote) {
		try {
			log.info("Parsing the CSV File in order to proceed with the actions.");
			CSVReader reader = new CSVReader(new FileReader(path), separator, quote);
			String [] nextLine = null;
			while ((nextLine = reader.readNext()) != null) {
				dbConnector = dbConnection.connectionDB();
				try {
					if (nextLine[0].equalsIgnoreCase("a")) {
						log.info("Adding Employee information to DB");
						dbFunction.insertDB(dbConnector, nextLine);
					}
					else if (nextLine[0].equalsIgnoreCase("r")) {
						log.info("Removing Employee from DB");
						dbFunction.removeDB(dbConnector, nextLine);
					}
					else if (nextLine[0].equalsIgnoreCase("m")) {
						log.info("Modifying Employee from DB");
						dbFunction.modifyDB(dbConnector, nextLine);
					}
					else {
						log.info("Wrong action in file (" + nextLine[0] + ").");
						JOptionPane.showMessageDialog(null,"Not a valid operation in file (" + nextLine[0] +") The actions must be 'a' or 'r'.");
					}
				} catch (SQLException e) {
					log.fatal("SQL Exception while performing DB Actions.");
					JOptionPane.showMessageDialog(null,"Problem found during Database actions.");
					e.printStackTrace();
				}
			}
			reader.close();
		} catch (FileNotFoundException e1) {
			log.fatal("File Not Found!");
			JOptionPane.showMessageDialog(null,"File was not found.");
			e1.printStackTrace();
			System.exit(0);
		} catch (IOException e1) {
			log.fatal("File Corrupted.");
			JOptionPane.showMessageDialog(null,"Error occured when processing the file.");
			e1.printStackTrace();
			System.exit(0);
		}
	}

	public void readFile(@NonNull String path) throws IOException {
		FileReader reader = null;
		try {
			log.info("Processing the file...");
			reader = new FileReader(path);
			log.info("Pasting file in textArea...");
			UploadFrame.textArea.read(reader, null);
			UploadFrame.btnUpload.setEnabled(true);
			UploadFrame.btnProcess.setEnabled(false);
			UploadFrame.btnClear.setEnabled(true);
		}
		catch (IOException exc) {
			JOptionPane.showMessageDialog(null,"Error occurred when processing the file.");
			log.fatal("Error in reading the file");
			UploadFrame.btnProcess.setEnabled(false);
			UploadFrame.btnClear.setEnabled(true);
			exc.printStackTrace();
		}
		finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	public void enableButtons(@NonNull String process) {
		if (process.equalsIgnoreCase("upload")) {
			log.debug("Changing Buttons status - Upload button pressed");
			fillComboBox();
			UploadFrame.btnProcess.setEnabled(true);
			UploadFrame.btnClear.setEnabled(true);
			UploadFrame.btnSaveInDb.setEnabled(false);
			UploadFrame.btnUpload.setEnabled(false);
			UploadFrame.btnShowAllEmployees.setEnabled(true);
		}
		else if (process.equalsIgnoreCase("process")) {
			log.debug("Changing Buttons status - Process button pressed");
			fillComboBox();
			UploadFrame.btnUpload.setEnabled(false);
			UploadFrame.btnClear.setEnabled(true);
			UploadFrame.btnSaveInDb.setEnabled(true);
			UploadFrame.btnProcess.setEnabled(false);
			UploadFrame.btnShowAllEmployees.setEnabled(true);
		}
		else if (process.equalsIgnoreCase("clear")) {
			log.debug("Changing Buttons status - Clear button pressed");
			fillComboBox();
			UploadFrame.btnUpload.setEnabled(true);
			UploadFrame.btnProcess.setEnabled(false);
			UploadFrame.btnSaveInDb.setEnabled(false);
			UploadFrame.btnClear.setEnabled(false);
			UploadFrame.btnShowAllEmployees.setEnabled(true);
		}
		else if (process.equalsIgnoreCase("save")) {
			log.debug("Changing Buttons status - Save button pressed");
			fillComboBox();
			UploadFrame.btnUpload.setEnabled(true);
			UploadFrame.btnProcess.setEnabled(false);
			UploadFrame.btnClear.setEnabled(false);
			UploadFrame.btnSaveInDb.setEnabled(false);
			UploadFrame.btnShowAllEmployees.setEnabled(true);
		}
		else if (process.equalsIgnoreCase("showall")) {
			log.debug("Changing Buttons status - Show All Info button pressed");
			fillComboBox();
			UploadFrame.btnUpload.setEnabled(true);
			UploadFrame.btnProcess.setEnabled(false);
			UploadFrame.btnClear.setEnabled(true);
			UploadFrame.btnSaveInDb.setEnabled(false);
			UploadFrame.btnShowAllEmployees.setEnabled(true);
		}
	}

	public void showAllInfo() throws SQLException{
		fillComboBox();
		dbConnector = dbConnection.connectionDB();
		ResultSet data;
		data = dbFunction.showDbInfo(dbConnector);
		DefaultTableModel aModel = (DefaultTableModel) UploadFrame.table.getModel();
		// Loop through the ResultSet and transfer in the Model
		java.sql.ResultSetMetaData rsmd = data.getMetaData();
		int colNo = rsmd.getColumnCount();
		int rowCount = aModel.getRowCount();
		//Remove rows one by one from the end of the table
		for (int i = rowCount - 1; i >= 0; i--) {
			aModel.removeRow(i);
		}
		while(data.next()){
			Object[] objects = new Object[colNo];
			for(int i=0;i<colNo;i++){
				objects[i]=data.getObject(i+1);
			}
			aModel.addRow(objects);
		}
	}

	public void setModelTable() {
		DefaultTableModel aModel = (DefaultTableModel) UploadFrame.table.getModel();
		aModel.addColumn("ID");
		aModel.addColumn("Name");
		aModel.addColumn("Surname");
		aModel.addColumn("Age");
		aModel.addColumn("Position");
		aModel.addColumn("Salary");
		UploadFrame.table.setModel(aModel);
	}

	public void fillComboBox() {
		ResultSet results = null;
		UploadFrame.comboBox.removeAllItems();
		dbConnector = dbConnection.connectionDB();
		results = dbFunction.findId(dbConnector);
		try {
			while (results.next()) {
				UploadFrame.comboBox.addItem(results.getInt("id"));
			}
		} catch (SQLException e) {
			log.fatal("SQL Exception while getting the id for removal.");
			e.printStackTrace();
		}
	}

	public void deleteByID() {
		dbConnector = dbConnection.connectionDB();
		dbFunction.removeById(dbConnector, UploadFrame.comboBox.getSelectedItem());
		fillComboBox();
	}
}
