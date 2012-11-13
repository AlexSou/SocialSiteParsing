/* A Soulier
 * Columbia University
 * April 2012
 * as4094@columbia.edu
 * Social Parser
 */

/* GUIg.java
 * This function shows the graphical user interface to the user, 
 * giving the user options on how to parse the websites and passing
 * the users choices to the control function.
 * 
 * Input: String -> current directory.
 */

package edu.columbia.smp;

import java.awt.EventQueue;

// Class that creates the graphic interface for the user to input options.
public class GUIg extends JFrame {

	static  String curDir;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// Function that starts the graphic interface.
	// Input: String -> Current directory.
	public static void main(String args) {
		curDir = args; //Pass current directory.
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI(); // Create frame.
					frame.setVisible(true);
				} catch (Exception e) { // Window creation error.
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		//Quit app if closed.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitle = new JLabel("Social Sites Parser"); //Title.
		lblTitle.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		lblTitle.setBounds(112, 21, 214, 16);
		contentPane.add(lblTitle);

		//Media sites checkboxes.
		final JCheckBox cbxAllEars = new JCheckBox("X1");
		cbxAllEars.setBounds(21, 49, 128, 23);
		contentPane.add(cbxX1);

		final JCheckBox cbxDFoodBlog = new JCheckBox("X2");
		cbxDFoodBlog.setBounds(21, 84, 155, 23);
		contentPane.add(cbxX2);

		final JCheckBox cbxFourSq = new JCheckBox("X3");
		cbxFourSq.setBounds(21, 119, 128, 23);
		contentPane.add(cbxX3);

		final JCheckBox cbxGMaps = new JCheckBox("Yelp");
		cbxGMaps.setBounds(21, 154, 128, 23);
		contentPane.add(cbxYelp);

	

		//Button to start 
		JButton btnStart = new JButton("Download");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { //Start button clicked.

				Boolean isX1, isX2, isYe;
				isX1 = false;
				isX2 = false;
				isYe = false;

				//No checkbox selected.
				if (!cbxX1.isSelected() && !cbxX2.isSelected() && !cbxYelp.isSelected() ) {
					JOptionPane.showMessageDialog(null, "Select at least 1 media page to parse.", null, JOptionPane.ERROR_MESSAGE); 					
				}
				else { //Select which sites to use.

					if(cbxX1.isSelected()) {
						isX1 = true;
					}
					if(cbxX2.isSelected()) {
						isX2 = true;
					}
		
					if(cbxYelp.isSelected()) {
						isYe = true;
					}
					Control control = new Control(isX1, isX2, isYe);
					try {
						control.exe(curDir);
					} catch (Exception e) { //Files not found error.

						JOptionPane.showMessageDialog(null, "Input files not present. " + e, null, JOptionPane.ERROR_MESSAGE);
					}
				}

			}
		});
		btnStart.setBounds(223, 119, 140, 46);
		contentPane.add(btnStart);
	}
}

// End of code.