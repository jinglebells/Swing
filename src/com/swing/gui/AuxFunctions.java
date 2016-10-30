package com.swing.gui;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	
	String[] information = null;
	String gender = null;
	String product = null;
	String height = null;
	String waist= null, hip= null, insideleg= null, bust= null, chest= null, legheight=null;
	String size = null;
	Integer adjust=null;

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

	public void sendPostRequest(String gender, String product, String height, BufferedImage image) throws Exception {
		final String USER_AGENT = "Mozilla/5.0";
		BufferedImage originalImage = image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( originalImage, "jpg", baos );
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		log.debug("Sending the message via POST.");
		URL url = new URL("http://10.1.0.161:8080");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Cache-Control", "no-cache");
		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
		writer.write("gender="+gender+"&product="+product+"&height="+height+"&image="+imageInByte);
		writer.flush();
		log.debug(conn.getOutputStream().toString());
		writer.close();
		int responseCode = conn.getResponseCode();
		log.debug("Sending 'POST' request to URL : " + url);
		log.debug("Response Code : " + responseCode);
		log.debug("Response Message : " + conn.getResponseMessage());
		log.debug("Response method: " + conn.getRequestMethod());
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		algorithm(response.toString());

	}

	private void algorithm(String response) {
		log.debug("Processing response from server.");
		information = response.split("_");
		gender = information[0];
		product = information[1];
		height = information[2];
		ProductDAO productDAO = new ProductDAO();
		
		if (gender.equalsIgnoreCase("male") && product.equalsIgnoreCase("pants")) {
			waist = information[3];
			insideleg = information[4];
			legheight = information[5];
			size =productDAO.getSizeMalePants(gender, product,waist,insideleg);
			if (Integer.parseInt(legheight) < Integer.parseInt(insideleg)) {
				adjust = Integer.parseInt(insideleg)-Integer.parseInt(legheight);
			}
			
			showingResultsPants(size, adjust);
		}
		else if (gender.equalsIgnoreCase("male") && product.equalsIgnoreCase("knits")) {
			chest = information[3];
			size =productDAO.getSizeMaleKnits(gender, product, chest);
			showingResults(size);
		}
		else if (gender.equalsIgnoreCase("female") && product.equalsIgnoreCase("pants")) {
			waist = information[3];
			hip = information[4];
			size =productDAO.getSizeFemalePants(gender, product, waist, hip);
			showingResults(size);
		}
		else if (gender.equalsIgnoreCase("female") && product.equalsIgnoreCase("knits")) {
			bust=information[3];
			size =productDAO.getSizeFemaleKnits(gender, product,bust);
			showingResults(size);
		}
		else if (gender.equalsIgnoreCase("female") && product.equalsIgnoreCase("dress")) {
			waist = information[3];
			bust = information[4];
			hip = information[5];
			size =productDAO.getSizeFemaleDress(gender, product, height, waist, hip,bust);
			showingResults(size);
		}
		
	}
	

	public static void closeFrame(Frame[] frames) {
		log.debug("Closing frames.");
		for (Frame frame : frames) {
			if (frame.getName().equalsIgnoreCase("frame0")) {
				frame.dispose();
			}
		}
	}
	
	public void showingResults(String size) {
		ResultFrame resultFrame = new ResultFrame();
		resultFrame.setEnabled(true);
		resultFrame.setVisible(true);
		ResultFrame.lblSize.setText(size);
//		closeFrame(LoadingFrame.getFrames());
	}
	
	private void showingResultsPants(String size, Integer adjust) {
		ResultFrame resultFrame = new ResultFrame();
		resultFrame.setEnabled(true);
		resultFrame.setVisible(true);
		ResultFrame.lblSize.setText(size);
		ResultFrame.txtpnAddinfo.setText("You should reduce " + adjust + "cm to your pants." );
//		closeFrame(LoadingFrame.getFrames());
		
	}
}
