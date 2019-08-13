package currencyTracker;


import java.awt.*;

import javax.swing.*;
public class MainWindow extends Canvas implements StandardVariables{
	
	/** draws the panel: assumption: Bounds and Background are already set
	 * 
	 */
	public MainWindow() {
		window = new JFrame("Currency Tracker");
		JComponent dI = new DrawInterface();
		dI.setOpaque(true);
		window.setContentPane(dI);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(FRAME_WIDTH,FRAME_HEIGHT);
		window.setBackground(Color.WHITE);
		window.pack();
		//window.setLayout(null);
		window.setVisible(true);
	}
	
	private JFrame window;
	
}
