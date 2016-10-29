package com.swing.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
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
import java.awt.Color;

public class UploadFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LogManager.getLogger(UploadFrame.class);

	static JPanel contentUpload;
	JTextField pathField;

	static JButton btnProcess;
	static JButton btnUpload;
	static JButton btnClear;
	JPanel imgPanel;

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
		setBounds(100, 100, 702, 512);
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

		//TextField - Path
		log.debug("Adding Path Field to GUI...");
		pathField = new JTextField();
		pathField.setBounds(94, 6, 200, 20);
		pathField.setEditable(false);
		pathField.setColumns(20);
		contentUpload.add(pathField);

		//Image Panel
		imgPanel = new JPanel();
		imgPanel.setBackground(Color.WHITE);
		imgPanel.setBounds(104, 74, 423, 306);
		contentUpload.add(imgPanel);


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
				log.debug("Button Upload pressed.");
				JFileChooser c = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "png", "jpg");
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
				log.debug("Button Process pressed.");
				//Save file in a new path
				try {
					aux.saveFile(pathField.getText());
                    BufferedImage image = ImageIO.read(new File(pathField.getText()));
                    BufferedImage newImg = resizeImage(image, imgPanel.getWidth(), imgPanel.getHeight(), 1);
                    ImageIcon icon = new ImageIcon(newImg);
                    JLabel label = new JLabel();
                    imgPanel.add(label);
                    label.setIcon(icon);
					process = "process";
					aux.enableButtons(process);
				} catch (Exception e1) {
					log.fatal("Errur during save file in DB.");
					JOptionPane.showMessageDialog(null,"Error during processing file.");
					e1.printStackTrace();
				}
			}
		});

		//Button Clear Actions
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				log.debug("Button Clear pressed.");
				process = "clear";
				aux.enableButtons(process);
				pathField.setText("");
				imgPanel.setBackground(Color.WHITE);
			}
		});
	}
	private BufferedImage resizeImage(BufferedImage originalImage, int width, int height, int type) throws IOException {  
        BufferedImage resizedImage = new BufferedImage(width, height, type);  
        Graphics2D g = resizedImage.createGraphics();  
        g.drawImage(originalImage, 0, 0, width, height, null);  
        g.dispose();  
        return resizedImage;  
    }  
}
