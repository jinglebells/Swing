package com.swing.tutorial;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.opencsv.CSVReader;

public class AuxFunctions {

	Logger log = Logger.getLogger(AuxFunctions.class.getName());
	
	DatabaseConnection dbConnection = new DatabaseConnection();
	DatabaseFunctions dbFunction = new DatabaseFunctions();
	
	Connection dbConnector;
	
	public void parseCSVFile(String path, char separator, char quote) {
		try {
			log.info("Parsing the CSV File in order to proceed with the actions.");
			CSVReader reader = new CSVReader(new FileReader(path), separator, quote);
			String [] nextLine = null;
			while ((nextLine = reader.readNext()) != null) {
				dbConnector = dbConnection.connectionDB();
				try {
					if (nextLine[0].equalsIgnoreCase("a")) {
						log.info("Adding Info to DB");
						dbFunction.insertDB(dbConnector, nextLine);
					}
					else if (nextLine[0].equalsIgnoreCase("r")) {
						log.info("Removing Info to DB");
						dbFunction.removeDB(dbConnector, nextLine);
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

	public void readFile(String path) {
		FileReader reader = null;
		try {
			reader = new FileReader(path);
			UploadFrame.textArea.read(reader, null);
			UploadFrame.btnUpload.setEnabled(true);
			UploadFrame.btnProcess.setEnabled(false);
			UploadFrame.btnClear.setEnabled(true);
		}
		catch (Exception exc) {
			UploadFrame.btnProcess.setEnabled(false);
			UploadFrame.btnClear.setEnabled(true);
		}
		finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null,"Error occurred when processing the file.");
					e1.printStackTrace();
					System.exit(0);
				}
			}
		}
	}

	public void enableButtons(String process) {
		if (process.equalsIgnoreCase("upload")) {
			UploadFrame.btnProcess.setEnabled(true);
			UploadFrame.btnClear.setEnabled(true);
			UploadFrame.btnSaveInDb.setEnabled(false);
			UploadFrame.btnUpload.setEnabled(false);
		}
		else if (process.equalsIgnoreCase("process")) {
			UploadFrame.btnUpload.setEnabled(false);
			UploadFrame.btnClear.setEnabled(true);
			UploadFrame.btnSaveInDb.setEnabled(true);
			UploadFrame.btnProcess.setEnabled(false);
		}
		else if (process.equalsIgnoreCase("clear")) {
			UploadFrame.btnUpload.setEnabled(true);
			UploadFrame.btnProcess.setEnabled(false);
			UploadFrame.btnSaveInDb.setEnabled(false);
			UploadFrame.btnClear.setEnabled(false);
		}
		else if (process.equalsIgnoreCase("save")) {
			UploadFrame.btnUpload.setEnabled(true);
			UploadFrame.btnProcess.setEnabled(false);
			UploadFrame.btnClear.setEnabled(false);
			UploadFrame.btnSaveInDb.setEnabled(false);
		}
	}
}
