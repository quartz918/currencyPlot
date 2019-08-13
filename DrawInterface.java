package currencyTracker;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class DrawInterface extends JPanel implements ActionListener, StandardVariables{
	public DrawInterface() {
		super(new BorderLayout());
		DateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd");
		JPanel parameterChooser = new JPanel(new GridLayout(0,7));
		JLabel currencyLabel = new JLabel("Currency:");
		JLabel startLabel = new JLabel("Start Date:");
		JLabel endLabel = new JLabel("End Date:");
	
		go = new JButton("Go");
		go.addActionListener(this);
		startDate = new JFormattedTextField(dateForm);
		
		endDate = new JFormattedTextField(dateForm);
		currency = new JTextField();
		parameterChooser.add(startLabel);
		parameterChooser.add(startDate);
		parameterChooser.add(endLabel);
		parameterChooser.add(endDate);
		parameterChooser.add(currencyLabel);
		parameterChooser.add(currency);
		parameterChooser.add(go);
		
		
		
		graph = new CurrencyGraph(50,50, FRAME_WIDTH-100, FRAME_HEIGHT-100);
		add(graph, BorderLayout.CENTER);
		add(parameterChooser, BorderLayout.SOUTH);
		
	}
	public void actionPerformed(ActionEvent e) {
		//System.out.println("Action Performed");
		if("Go".contentEquals(e.getActionCommand())){
			if(checkInput()) {
				String options = createExchangeRatesAPIOptions();
				getValues();
				CurrencyDataRequest data = new CurrencyDataRequest(options, "multi");
				graph.feedData(data, curD, sD, eD);
				graph.repaint();
			}
			
		}
	}
	
	/** Get values from text field and parse in useful format */
	public void getValues() {
		sD = startDate.getText();
		eD = endDate.getText();
		curD = currency.getText();	
	}
	
	private String createExchangeRatesAPIOptions(){
		String result = "history?start_at=" + startDate.getText() + "&end_at=" + endDate.getText();
		System.out.println(result);
		return result;
		
	}
	/* Check user input - to be implemented later */
	private boolean checkInput() {
		return true;
	}
	private String curD;
	private String sD;
	private String eD;
	private JFormattedTextField startDate;
	private JFormattedTextField endDate;
	private JTextField currency;
	private JButton go;
	private CurrencyGraph graph;
}
