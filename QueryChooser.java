package currencyTracker;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class QueryChooser extends JPanel {
	public QueryChooser() {
		super(new BorderLayout());
		JPanel parameterChooser = new JPanel(new GridLayout(0,7));
		JLabel currencyLabel = new JLabel("Currency:");
		JLabel startLabel = new JLabel("Start Date:");
		JLabel endLabel = new JLabel("End Date:");
		go = new JButton("Go");
		startDate = new JFormattedTextField();
		startDate.setColumns(10);
		endDate = new JFormattedTextField();
		currency = new JFormattedTextField();
		parameterChooser.add(startLabel);
		parameterChooser.add(startDate);
		parameterChooser.add(endLabel);
		parameterChooser.add(endDate);
		parameterChooser.add(currencyLabel);
		parameterChooser.add(currency);
		parameterChooser.add(go);
		add(parameterChooser);
	}

	
	
	
	private JFormattedTextField startDate;
	private JFormattedTextField endDate;
	private JFormattedTextField currency;
	public JButton go;
}
