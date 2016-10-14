package com.swing.tutorial;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.opencsv.CSVReader;

public class AuxFunctions {

	private static final Logger log = LogManager.getLogger(AuxFunctions.class);

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
						log.info("Adding Employee information to DB");
						dbFunction.insertDB(dbConnector, nextLine);
					}
					else if (nextLine[0].equalsIgnoreCase("r")) {
						log.info("Removing Employee from DB");
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
				} finally {
					if (reader != null) {
						reader.close();
					}
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

	public void readFile(String path) throws IOException {
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

	public void enableButtons(String process) {
		if (process.equalsIgnoreCase("upload")) {
			log.debug("Changing Buttons status - Upload button pressed");
			UploadFrame.btnProcess.setEnabled(true);
			UploadFrame.btnClear.setEnabled(true);
			UploadFrame.btnSaveInDb.setEnabled(false);
			UploadFrame.btnUpload.setEnabled(false);
		}
		else if (process.equalsIgnoreCase("process")) {
			log.debug("Changing Buttons status - Process button pressed");
			UploadFrame.btnUpload.setEnabled(false);
			UploadFrame.btnClear.setEnabled(true);
			UploadFrame.btnSaveInDb.setEnabled(true);
			UploadFrame.btnProcess.setEnabled(false);
		}
		else if (process.equalsIgnoreCase("clear")) {
			log.debug("Changing Buttons status - Clear button pressed");
			UploadFrame.btnUpload.setEnabled(true);
			UploadFrame.btnProcess.setEnabled(false);
			UploadFrame.btnSaveInDb.setEnabled(false);
			UploadFrame.btnClear.setEnabled(false);
		}
		else if (process.equalsIgnoreCase("save")) {
			log.debug("Changing Buttons status - Save button pressed");
			UploadFrame.btnUpload.setEnabled(true);
			UploadFrame.btnProcess.setEnabled(false);
			UploadFrame.btnClear.setEnabled(false);
			UploadFrame.btnSaveInDb.setEnabled(false);
		}
	}
}
