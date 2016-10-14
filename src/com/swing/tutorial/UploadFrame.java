package com.swing.tutorial;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class UploadFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	 private static final Logger log = LogManager.getLogger(UploadFrame.class);
	
	JPanel contentPane;
	JTextField pathField;
	static JTextArea textArea;

	static JButton btnProcess;
	static JButton btnUpload;
	static JButton btnClear;
	static JButton btnSaveInDb;

	JLabel lblYourFileContents;

	private char CSV_SEPARATOR = ',';
	private char CSV_QUOTE = '\"';
	
	String process;

	AuxFunctions aux = new AuxFunctions();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UploadFrame frame = new UploadFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public UploadFrame() {

		//Content
		log.debug("Adding Content to GUI...");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 702, 490);
		ImageIcon webIcon = new ImageIcon("resources\\mtrust.png");
		setIconImage(webIcon.getImage());

		//Buttons
		//Upload Button
		log.debug("Adding Upload Button to GUI...");
		btnUpload = new JButton("Upload");
		btnUpload.setBounds(5, 5, 84, 23);
		btnUpload.setEnabled(true);
		contentPane.add(btnUpload);
		//Process Button
		log.debug("Adding Process Button to GUI...");
		btnProcess = new JButton("Process");
		btnProcess.setEnabled(false);
		btnProcess.setBounds(5, 39, 84, 23);
		contentPane.add(btnProcess);
		//Clear Button
		log.debug("Adding Clear Button to GUI...");
		btnClear = new JButton("Clear");
		btnClear.setBounds(94, 39, 89, 23);
		btnClear.setEnabled(false);
		contentPane.add(btnClear);
		//Save in DB Button
		log.debug("Adding Save in DB Button to GUI...");
		btnSaveInDb = new JButton("Commit Actions in DB");
		btnSaveInDb.setBounds(5, 225, 165, 23);
		btnSaveInDb.setEnabled(false);
		contentPane.add(btnSaveInDb);

		//TextField - Path
		log.debug("Adding Path Field to GUI...");
		pathField = new JTextField();
		pathField.setBounds(94, 6, 200, 20);
		pathField.setEditable(false);
		pathField.setColumns(20);
		contentPane.add(pathField);

		//JTextArea
		log.debug("Adding Text Area to GUI...");
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		JScrollPane scroll = new JScrollPane (textArea, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(5, 110, 661, 104);
		contentPane.add(scroll);

		//Labels
		//YourContents
		log.debug("Adding Label to GUI...");
		lblYourFileContents = new JLabel("Your File Contents");
		lblYourFileContents.setBounds(5, 85, 89, 14);
		contentPane.add(lblYourFileContents);

		//MenuBar
		log.debug("Adding Menu Bar to GUI...");
		JMenuBar menubar = new JMenuBar();
		ImageIcon icon = new ImageIcon("resources\\exit.png");
		//resize do icone
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);

		//FileField in MenuBar
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		//File Option
		JMenuItem eMenuItem = new JMenuItem("Exit", icon);
		eMenuItem.setMnemonic(KeyEvent.VK_E);
		eMenuItem.setToolTipText("Exit application");
		eMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(eMenuItem);
		menubar.add(fileMenu);
		setJMenuBar(menubar);

		//Button Upload Actions
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser c = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
				c.setFileSelectionMode(JFileChooser.FILES_ONLY);
				c.setFileFilter(filter);
				int rVal = c.showOpenDialog(UploadFrame.this);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					pathField.setText(c.getSelectedFile().getAbsolutePath());
					pathField.setPreferredSize(new Dimension((pathField.getText().length()*100), 20));
					process = "upload";
					aux.enableButtons(process);
				}
			}
		});

		//Button Process Actions
		btnProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					aux.readFile(pathField.getText());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null,"Error occurred when processing the file.");
					log.fatal("Error in reading the file");
					UploadFrame.btnProcess.setEnabled(false);
					UploadFrame.btnClear.setEnabled(true);
					e1.printStackTrace();
				}
				process = "process";
				aux.enableButtons(process);
			}
		});

		//Button Clear Actions
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				process = "clear";
				aux.enableButtons(process);
				textArea.setText("");
				pathField.setText("");
			}
		});

		//Button Save in DB Actions
		btnSaveInDb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aux.parseCSVFile(pathField.getText(), CSV_SEPARATOR, CSV_QUOTE);
				process = "save";
				aux.enableButtons(process);
				textArea.setText("");
				pathField.setText("");
			}
		});
	}
}
