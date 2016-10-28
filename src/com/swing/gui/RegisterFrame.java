package com.swing.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.Frame;
import java.awt.event.ActionEvent;

public class RegisterFrame extends JFrame{


	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(RegisterFrame.class);

	static JPasswordField pwdNewpassword;
	static JTextField usernameField;
	static JTextField firstNameField;
	static JTextField lastNameField;
	static JTextField emailField;
	static JTextField phoneNumberField;

	JButton btnRegister;
	JButton btnCheck;

	JLabel lblUsername;
	JLabel lblPassword;
	JLabel lblFirstName;
	JLabel lblLastName;
	JLabel lblEmail;
	JLabel lblPhoneNumber;

	AuxFunctions aux = new AuxFunctions();

	public RegisterFrame() {

		log.debug("Adding Content Register to GUI...");
		getContentPane().setLayout(null);

		//Labels
		log.debug("Adding Register labels to GUI...");
		//Username
		lblUsername = new JLabel("Username:");
		lblUsername.setBounds(10, 23, 90, 14);
		getContentPane().add(lblUsername);
		//Password
		lblPassword = new JLabel("Password:");
		lblPassword.setBounds(10, 48, 90, 14);
		getContentPane().add(lblPassword);
		//FirstName
		lblFirstName = new JLabel("First Name:");
		lblFirstName.setBounds(10, 73, 90, 14);
		getContentPane().add(lblFirstName);
		//LastName
		lblLastName = new JLabel("Last Name:");
		lblLastName.setBounds(10, 98, 90, 14);
		getContentPane().add(lblLastName);
		//Email
		lblEmail = new JLabel("E-mail:");
		lblEmail.setBounds(10, 123, 90, 14);
		getContentPane().add(lblEmail);
		//PhoneNumber
		lblPhoneNumber = new JLabel("Phone Number:");
		lblPhoneNumber.setBounds(10, 148, 90, 14);
		getContentPane().add(lblPhoneNumber);

		//TextFields
		log.debug("Adding Register textfields to GUI...");
		//Password
		pwdNewpassword = new JPasswordField();
		pwdNewpassword.setBounds(110, 45, 90, 20);
		getContentPane().add(pwdNewpassword);
		//Username
		usernameField = new JTextField();
		usernameField.setBounds(110, 20, 90, 20);
		getContentPane().add(usernameField);
		usernameField.setColumns(10);
		//FirstName
		firstNameField = new JTextField();
		firstNameField.setBounds(110, 70, 90, 20);
		getContentPane().add(firstNameField);
		firstNameField.setColumns(10);
		//LastName
		lastNameField = new JTextField();
		lastNameField.setBounds(110, 95, 90, 20);
		getContentPane().add(lastNameField);
		lastNameField.setColumns(10);
		//Email
		emailField = new JTextField();
		emailField.setBounds(110, 120, 90, 20);
		getContentPane().add(emailField);
		emailField.setColumns(10);
		//PhoneNumber
		phoneNumberField = new JTextField();
		phoneNumberField.setBounds(110, 145, 90, 20);
		getContentPane().add(phoneNumberField);
		phoneNumberField.setColumns(10);

		//Buttons
		log.debug("Adding Register Buttons to GUI...");
		//Register
		btnRegister = new JButton("Register");
		btnRegister.setBounds(59, 187, 89, 23);
		getContentPane().add(btnRegister);
		//Check
		btnCheck = new JButton("Check");
		btnCheck.setBounds(210, 19, 89, 23);
		getContentPane().add(btnCheck);

		//Actions
		//Button Register
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					aux.registerDB();
					UploadFrame uploadFrame = new UploadFrame();
					uploadFrame.setEnabled(true);
					uploadFrame.setVisible(true);
					closeFrame(RegisterFrame.getFrames());
					JOptionPane.showMessageDialog(null,"User: " + usernameField.getText() + " was created.");
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Error during registering your user. Please try again later.");
					e1.printStackTrace();
				}
			}
		});
	}
	
	public static void closeFrame(Frame[] frames) {
		for (Frame frame : frames) {
			if (frame.getName().equalsIgnoreCase("frame0")) {
				frame.dispose();
			}
		}
	}
}
