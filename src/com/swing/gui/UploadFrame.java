package com.swing.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.JTableHeader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UploadFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LogManager.getLogger(UploadFrame.class);

	static JPanel contentUpload;
	JTextField pathField;
	static JTextArea textArea;
	static JTable table;

	static JButton btnProcess;
	static JButton btnUpload;
	static JButton btnClear;
	static JButton btnSaveInDb;
	static JButton btnShowAllEmployees;
	JButton btnDelete;

	JLabel lblYourFileContents;
	JLabel lblDeleteById;
	
	static JComboBox<Object> comboBox;

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
		contentUpload = new JPanel();
		contentUpload.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentUpload);
		contentUpload.setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 702, 691);
		ImageIcon webIcon = new ImageIcon("resources\\mtrust.png");
		setIconImage(webIcon.getImage());


		//Buttons
		//Upload Button
		log.debug("Adding Upload Button to GUI...");
		btnUpload = new JButton("Upload");
		btnUpload.setBounds(5, 5, 84, 23);
		btnUpload.setEnabled(true);
		contentUpload.add(btnUpload);
		//Process Button
		log.debug("Adding Process Button to GUI...");
		btnProcess = new JButton("Process");
		btnProcess.setEnabled(false);
		btnProcess.setBounds(5, 39, 84, 23);
		contentUpload.add(btnProcess);
		//Clear Button
		log.debug("Adding Clear Button to GUI...");
		btnClear = new JButton("Clear");
		btnClear.setBounds(94, 39, 89, 23);
		btnClear.setEnabled(false);
		contentUpload.add(btnClear);
		//Save in DB Button
		log.debug("Adding Save in DB Button to GUI...");
		btnSaveInDb = new JButton("Commit Actions in DB");
		btnSaveInDb.setBounds(5, 225, 165, 23);
		btnSaveInDb.setEnabled(false);
		contentUpload.add(btnSaveInDb);
		//Show All Info Button
		log.debug("Adding Show All Info Button to GUI...");
		btnShowAllEmployees = new JButton("Show All Employes");
		btnShowAllEmployees.setBounds(5, 259, 165, 23);
		btnShowAllEmployees.setEnabled(true);
		contentUpload.add(btnShowAllEmployees);
		//Delete by ID Button
		btnDelete = new JButton("Delete");
		btnDelete.setBounds(112, 436, 89, 23);
		btnDelete.setEnabled(true);
		contentUpload.add(btnDelete);

		//TextField - Path
		log.debug("Adding Path Field to GUI...");
		pathField = new JTextField();
		pathField.setBounds(94, 6, 200, 20);
		pathField.setEditable(false);
		pathField.setColumns(20);
		contentUpload.add(pathField);

		//JTextArea
		log.debug("Adding Text Area to GUI...");
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		JScrollPane scroll = new JScrollPane (textArea, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(5, 110, 661, 104);
		contentUpload.add(scroll);

		//Labels
		//YourContents
		log.debug("Adding Label to GUI...");
		lblYourFileContents = new JLabel("Your File Contents");
		lblYourFileContents.setBounds(5, 85, 89, 14);
		contentUpload.add(lblYourFileContents);
		//DeleteByID
		lblDeleteById = new JLabel("Delete by ID");
		lblDeleteById.setBounds(5, 440, 66, 14);
		contentUpload.add(lblDeleteById);

		//Table
		table = new JTable();
		JTableHeader header = table.getTableHeader();
		header.setFont(new Font("Dialog", Font.BOLD, 14));
		JScrollPane js=new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		js.setVisible(true);
		js.setBounds(5, 303, 661, 116);
		aux.setModelTable();
		contentUpload.add(js);
		
		//ComboBox
		comboBox = new JComboBox<Object>();
		comboBox.setBounds(76, 437, 28, 20);
		aux.fillComboBox();
		contentUpload.add(comboBox);
		
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
				for (int i = 0; i < table.getRowCount(); i++){
					for(int j = 0; j < table.getColumnCount(); j++) {
						table.setValueAt("", i, j);
					}
				}
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

		//Button ShowAll Actions
		btnShowAllEmployees.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					aux.showAllInfo();
					process = "showAll";
					aux.enableButtons(process);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		//Button delete by ID
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aux.deleteByID();
			}
		});
	}
}
