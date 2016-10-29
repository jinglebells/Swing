package com.swing.gui;

import java.awt.HeadlessException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opencsv.CSVReader;
import com.swing.dao.FileDAO;
import com.swing.dao.ProductDAO;
import com.swing.dao.UserDAO;
import com.swing.database.DatabaseConnection;
import com.swing.database.DatabaseFunctions;
import com.swing.entity.File;
import com.swing.entity.User;

import lombok.NonNull;

public class AuxFunctions {

	private static final Logger log = LogManager.getLogger(AuxFunctions.class);

	DatabaseConnection dbConnection = new DatabaseConnection();
	DatabaseFunctions dbFunction = new DatabaseFunctions();

	Connection dbConnector;

	public void login(@NonNull String username, @NonNull String password) throws NoSuchAlgorithmException, HeadlessException, SQLException {
		log.info("Trying to login.");
		dbConnector = dbConnection.connectionDB();
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		String md5 = new BigInteger(1, md.digest()).toString(16); // Hash value
		if (dbFunction.loginDB(dbConnector, username, md5)) {
			JOptionPane.showMessageDialog(null,"Login Successfull");
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
	}

	public void enableButtons(@NonNull String process) {
		if (process.equalsIgnoreCase("upload")) {
			log.debug("Changing Buttons status - Upload button pressed");
			UploadFrame.btnProcess.setEnabled(true);
			UploadFrame.btnClear.setEnabled(true);
			UploadFrame.btnUpload.setEnabled(false);
		}
		else if (process.equalsIgnoreCase("process")) {
			log.debug("Changing Buttons status - Process button pressed");
			UploadFrame.btnUpload.setEnabled(false);
			UploadFrame.btnClear.setEnabled(true);
			UploadFrame.btnProcess.setEnabled(false);
		}
		else if (process.equalsIgnoreCase("clear")) {
			log.debug("Changing Buttons status - Clear button pressed");
			UploadFrame.btnUpload.setEnabled(true);
			UploadFrame.btnProcess.setEnabled(false);
			UploadFrame.btnClear.setEnabled(false);
		}

	}


	@SuppressWarnings("deprecation")
	public void registerDB() throws Exception {
		log.debug("Going to register teh user in DB.");
		User newUser = new User();
		newUser.setFirstName(RegisterFrame.firstNameField.getText());
		newUser.setLastName(RegisterFrame.lastNameField.getText());
		newUser.setUsername(RegisterFrame.usernameField.getText());
		newUser.setPassword(RegisterFrame.pwdNewpassword.getText());
		newUser.setEmail(RegisterFrame.emailField.getText());
		newUser.setPhoneNumber(RegisterFrame.phoneNumberField.getText());

		UserDAO userDAO = new UserDAO();
		userDAO.insert(newUser);

	}

	public boolean findByUsername() throws Exception {
		log.debug("Finding the User by Username.");
		UserDAO userDAO = new UserDAO();
		if (userDAO.checkUser(RegisterFrame.usernameField.getText()) == true) {
			return true;
		}
		else {
			return false;
		}

	}

	public void saveFile(String text) throws Exception {
		//TODO Save File in machine


		//save File info in DB.
		//getName of file
		String [] names = text.split("/");
		File newFile = new File();
		newFile.setFilename(names[names.length-1]);
		//current date
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		newFile.setUploadedTime(dateFormat.format(date));
		//path
		newFile.setPath(text);
		FileDAO fileDAO = new FileDAO();
		fileDAO.insert(newFile);
	}

	public void getProducts() throws Exception {
		ProductDAO productDAO = new ProductDAO();
		HashSet<String> products = productDAO.getProducts();

		for ( String s : products) {
			UploadFrame.choiceProduct.add(s);
		}
	}
}
