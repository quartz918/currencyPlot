package currencyTracker;

import java.awt.*;
import java.awt.geom.Line2D;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;



public class CurrencyGraph extends JComponent implements StandardVariables{
	public CurrencyGraph(int posX, int posY, int cWidth, int cHeight) {
		x = posX;
		y = posY;
		width = cWidth;
		height = cHeight;
		data = null;
	}
	
	/** Draws the graph
	 * 
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		/* draw grid */
		double xLength = getWidth() - 2 * WIDTH_OFFSET;
		double yLength = getHeight() - 2 * HEIGHT_OFFSET;
		Line2D xAxis = new Line2D.Double(0 + WIDTH_OFFSET, yLength + HEIGHT_OFFSET, xLength + WIDTH_OFFSET, yLength + HEIGHT_OFFSET);
		Line2D yAxis = new Line2D.Double(0 + WIDTH_OFFSET, HEIGHT_OFFSET, 0 + WIDTH_OFFSET, yLength + HEIGHT_OFFSET);
		g2.draw(xAxis);
		g2.draw(yAxis);

		if(data != null) {
			double max = data.getMax(currency); // determine max value of currency
			double numDataSets = data.getNumDataSets();
			double tickDist = xLength / numDataSets;
			
			/* graph on day-to-day basis -- extend with method for graph based on time stamps */
			try {
				
				/* set variables */
				Date sD = new SimpleDateFormat("yyy-MM-dd").parse(startDate);
				Date eD = new SimpleDateFormat("yyy-MM-dd").parse(endDate);
				DateFormat dFormat = new SimpleDateFormat("yyy-MM-dd");
				double startValue = 0;
				double endValue = 0.;
				
				/* determine first startValue. Iterate till first date found */
				do {
					if(data.getRates(dFormat.format(sD))!= null && data.getRates(dFormat.format(sD)).containsKey(currency))
						startValue = data.getRates(dFormat.format(sD)).get(currency).doubleValue();
					Calendar c = Calendar.getInstance(); 
					c.setTime(sD); 
					c.add(Calendar.DATE, 1);
					sD = c.getTime();
				} while(data.getRates(dFormat.format(sD))== null);
				// System.out.println(startValue);
				
				/* Draw graph -- move to separate method */
				int counter = 0;
				for(Date dIt = sD; dIt.compareTo(eD) < 0;) {
					if(data.getRates(dFormat.format(dIt))!= null && data.getRates(dFormat.format(dIt)).containsKey(currency)) {
						endValue = data.getRates(dFormat.format(dIt)).get(currency).doubleValue();
						counter++;
					}
					Calendar c = Calendar.getInstance(); 
					c.setTime(dIt); 
					c.add(Calendar.DATE, 1);
					dIt = c.getTime();
					
					Line2D tick = new Line2D.Double(WIDTH_OFFSET + (counter) * tickDist, getHeight()- HEIGHT_OFFSET - startValue / max * yLength , WIDTH_OFFSET + (counter) * tickDist, getHeight()- HEIGHT_OFFSET - endValue / max * yLength );
					g2.draw(tick);
					startValue = endValue;
					
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void feedData(CurrencyDataRequest data, String cur, String sD, String eD) {
		this.data = data;
		currency = cur;
		startDate = sD;
		endDate = eD;
	}
	
	private int x;
	private int y;
	private int width;
	private int height;
	private String startDate;
	private String endDate;
	private CurrencyDataRequest data;
	private String currency;
}
