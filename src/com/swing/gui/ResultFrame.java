package com.swing.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;

public class ResultFrame extends JFrame{
	static JLabel lblSizeYouShould;
	static JLabel lblSize;
	static JLabel lblAdditionalInformation;
	static JTextPane txtpnAddinfo;
	
	public ResultFrame() {
		getContentPane().setLayout(null);
		setBounds(12, 12, 450, 180);
		
		lblSizeYouShould = new JLabel("Size you should buy:");
		lblSizeYouShould.setBounds(12, 12, 165, 15);
		getContentPane().add(lblSizeYouShould);
		
		lblSize = new JLabel("");
		lblSize.setBounds(175, 12, 70, 15);
		getContentPane().add(lblSize);
		
		lblAdditionalInformation = new JLabel("Additional Information:");
		lblAdditionalInformation.setBounds(12, 41, 191, 15);
		getContentPane().add(lblAdditionalInformation);
		
		txtpnAddinfo = new JTextPane();
		txtpnAddinfo.setBounds(206, 27, 215, 112);
		getContentPane().add(txtpnAddinfo);
	}
}
