package com.swing.gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegisterFrame extends JFrame{


	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(RegisterFrame.class);

	static JPasswordField pwdNewpassword;
	static JTextField usernameField;
	static JTextField firstNameField;
	static JTextField lastNameField;
	static JTextField emailField;
	static JFormattedTextField phoneNumberField;

	JButton btnRegister;
	JButton btnCheck;

	JLabel lblUsername;
	JLabel lblPassword;
	JLabel lblFirstName;
	JLabel lblLastName;
	JLabel lblEmail;
	JLabel lblPhoneNumber;
	JLabel lblCheckuser;

	AuxFunctions aux = new AuxFunctions();

	public RegisterFrame() {

		log.debug("Adding Content Register to GUI...");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 379, 258);
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
		//CheckUser
		lblCheckuser = new JLabel("");
		lblCheckuser.setBounds(210, 48, 179, 14);
		getContentPane().add(lblCheckuser);

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

		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setAllowsInvalid(false);
		// If you want the value to be committed on each keystroke instead of focus lost
		formatter.setCommitsOnValidEdit(true);
		phoneNumberField = new JFormattedTextField(formatter);
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
		btnCheck.setBounds(230, 19, 89, 23);
		getContentPane().add(btnCheck);


		//Actions
		//Button Register
		btnRegister.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				log.debug("Button Register pressed.");
				log.debug("Checking if all fields are completed.");
				if ((usernameField.getText() != null && !usernameField.getText().isEmpty()) &&
						(pwdNewpassword.getText() != null && !pwdNewpassword.getText().isEmpty()) &&
						(firstNameField.getText() != null && !firstNameField.getText().isEmpty()) &&
						(lastNameField.getText() != null && !lastNameField.getText().isEmpty()) &&
						(emailField.getText() != null && !emailField.getText().isEmpty()) &&
						(phoneNumberField.getText().toString() != null && !phoneNumberField.getText().isEmpty())) {
					if (!emailField.getText().contains("@")) {
						log.debug("Checking if email contains the @.");
						JOptionPane.showMessageDialog(null,"You must insert a valid email.");
						return;
					}

					try {
						if (aux.findByUsername() ==true) {
							log.debug("Checking the username before committing in DB.");
							JOptionPane.showMessageDialog(null,"This username is already in use.");
						}
						else {
							aux.registerDB();
							UploadFrame uploadFrame = new UploadFrame();
							uploadFrame.setEnabled(true);
							uploadFrame.setVisible(true);
							closeFrame(RegisterFrame.getFrames());
							JOptionPane.showMessageDialog(null,"User: " + usernameField.getText() + " was created.");
						}

					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null,"Error during registering your user. Please try again later.");
						e1.printStackTrace();
					}
				}
				else {
					JOptionPane.showMessageDialog(null,"You must fill all fields.");
				}
			}
		});
		//Button Check
		btnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					log.debug("Button Check pressed.");
					if (aux.findByUsername() ==true) {
						//label = username already existing
						lblCheckuser.setText("Username already exists.");
					}
					else {
						//label = username can be used
						lblCheckuser.setText("Username can be used.");
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null,"Error checking username. Please try again later.");
					e2.printStackTrace();
				}
			}
		});
	}

	public static void closeFrame(Frame[] frames) {
		log.debug("Closing Frames.");
		for (Frame frame : frames) {
			if (frame.getName().equalsIgnoreCase("frame0")) {
				frame.dispose();
			}
		}
	}
}
