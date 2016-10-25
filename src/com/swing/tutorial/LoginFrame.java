package com.swing.tutorial;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.JPasswordField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class LoginFrame extends JFrame {
	
	private static final Logger log = LogManager.getLogger(LoginFrame.class);
	
	
	JPanel contentLogin;
	
	private JTextField username;
	private JPasswordField pwdPassword;
	
	JButton btnLogin;
	JButton btnRegister;
	
	JLabel lblUsername;
	JLabel lblPassword;
	
	AuxFunctions aux = new AuxFunctions();
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public LoginFrame() {
		
		//Content
		log.debug("Adding Content Login to GUI...");
		contentLogin = new JPanel();
		setContentPane(contentLogin);
		contentLogin.setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 326, 167);

		//Buttons
		//Button Login
		btnLogin = new JButton("Login");
		btnLogin.setBounds(117, 96, 89, 23);
		contentLogin.add(btnLogin);
		//Button Register
		btnRegister = new JButton("Register");
		btnRegister.setBounds(212, 96, 89, 23);
		contentLogin.add(btnRegister);

		//Labels
		//Label Username
		lblUsername = new JLabel("Username:");
		lblUsername.setBounds(10, 14, 88, 14);
		lblUsername.setPreferredSize(new Dimension((lblUsername.getText().length()*100), 20));
		contentLogin.add(lblUsername);
		//Label Password
		lblPassword = new JLabel("Password:");
		lblPassword.setBounds(10, 42, 88, 14);
		lblPassword.setPreferredSize(new Dimension((lblPassword.getText().length()*100), 20));
		contentLogin.add(lblPassword);
		
		//TextFields
		//Username TextFiels
		username = new JTextField();
		username.setBounds(108, 11, 142, 20);
		username.setColumns(10);
		contentLogin.add(username);
		//Password Field
		pwdPassword = new JPasswordField();
		pwdPassword.setBounds(108, 42, 142, 20);
		pwdPassword.setText("password");
		contentLogin.add(pwdPassword);
		
		//Actions
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aux.login(username.getText(), pwdPassword.getText());
			}
		});

	}
}

