package currencyTracker;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;



public class DrawInterface extends JPanel implements ActionListener, StandardVariables{
	public DrawInterface(String inputSource) {
		super(new BorderLayout());
		dataSource = inputSource;
		DateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd");
		JPanel parameterChooser = new JPanel(new GridLayout(0,7));
		
		JLabel currencyLabel = new JLabel("Currency:");
		JLabel startLabel = new JLabel("Start Date:");
		JLabel endLabel = new JLabel("End Date:");
		currencyLabel.setLabelFor(currency);
		startLabel.setLabelFor(startDate);
		endLabel.setLabelFor(endDate);
		go = new JButton("Go");
		go.addActionListener(this);
		startDate = new JFormattedTextField(dateForm);
		
		endDate = new JFormattedTextField(dateForm);
		currency = new JTextField();
		
		// do not show start and end date field for alphavantage.co
		if(!dataSource.equals("alphavantage.co")) {
			parameterChooser.add(startLabel);
			parameterChooser.add(startDate);
			parameterChooser.add(endLabel);
			parameterChooser.add(endDate);
		}
		parameterChooser.add(currencyLabel);
		parameterChooser.add(currency);
		parameterChooser.add(go);
		
		setPreferredSize(new Dimension(1000, 500));
		setOpaque(true);
		setBackground(Color.WHITE);
		graph = new CurrencyGraph();
		add(graph, BorderLayout.CENTER);
		add(parameterChooser, BorderLayout.SOUTH);
		
	}
	public void actionPerformed(ActionEvent e) {
		//System.out.println("Action Performed");
		if("Go".contentEquals(e.getActionCommand())){
			if(checkInput()) {
				// get necessary values to plot
				getValues();
				String options = getOptions();
				
				// get the currency data
				CurrencyDataRequest data = new CurrencyDataRequest(dataSource, options, 2);
				ArrayList <Double> dt = data.getData(curD);
				ArrayList <Date> timeline = data.getTimeLine();
				
				// plot
				graph.feedData(true, timeline, dt, curD);
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
	
	
	/** Creates the query */
	private String getOptions() {
		String options = "";
		if(dataSource.equals("exchangerateapi.io")) {
			options = createExchangeRatesAPIOptions();
		}
		else if(dataSource.equals("alphavantage.co")) {
			options = createAlphavantageOptions();
		}
		return options;
	}
	
	/** Query for exchangeratesapi.io */
	private String createExchangeRatesAPIOptions(){
		String result = "history?start_at=" + startDate.getText() + "&end_at=" + endDate.getText();
		//System.out.println(result);
		return result;
		
	}
	
	/** Query for alphavantage.co */
	private String createAlphavantageOptions(){
		File file = new File("apikeyAlpha.txt");
		String key = "";
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new FileReader(file));
			key = rd.readLine();
			rd.close();
		}
		
		catch(IOException ex1){
			System.out.println("Error while reading apikey");
			
		}
		
		String function = "function=FX_DAILY";
		String from_currency = "from_symbol=EUR";
		String to_currency = "to_symbol=" + curD;
		String apikey = "apikey=" + key;
		String result = "query?" + function +"&" + from_currency + "&" + to_currency + "&" + apikey;
		
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
	private String dataSource;
}
