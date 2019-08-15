package currencyTracker;

import java.awt.*;
import java.awt.geom.Line2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;


/** JPanel with graph of requested currency data */
public class CurrencyGraph extends JPanel implements StandardVariables{
	public CurrencyGraph() {
		plot = false;
		plotOption = 't';
		setLayout(null);
		setBackground(Color.WHITE);
	}
	
	/** Draws the graph
	 * 
	 */
	@Override
	public void paintComponent(Graphics g) {
		removeAll();
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		/* draw grid */
		double xLength = getWidth() - 2 * WIDTH_OFFSET;
		double yLength = getHeight() - 2 * HEIGHT_OFFSET;
		Line2D xAxis = new Line2D.Double(0 + WIDTH_OFFSET, yLength + HEIGHT_OFFSET, xLength + WIDTH_OFFSET, yLength + HEIGHT_OFFSET);
		Line2D yAxis = new Line2D.Double(0 + WIDTH_OFFSET, HEIGHT_OFFSET, 0 + WIDTH_OFFSET, yLength + HEIGHT_OFFSET);
		g2.draw(xAxis);
		g2.draw(yAxis);
		
		int numSteps = 10;
		double yMeassureStep = yLength / numSteps; 
		double xMeassureStep = xLength / numSteps;
		for(int i = 0; i < 10; i++) {
			Line2D yMeassure = new Line2D.Double(WIDTH_OFFSET-5, HEIGHT_OFFSET + (i+1) * yMeassureStep, WIDTH_OFFSET+5, HEIGHT_OFFSET + (i+1) * yMeassureStep);
			Line2D xMeassure = new Line2D.Double(WIDTH_OFFSET + (i) * xMeassureStep, HEIGHT_OFFSET + yLength-5, WIDTH_OFFSET + (i) * xMeassureStep, HEIGHT_OFFSET + yLength+5);
			
			if(plot) {
				// create labels on axis
				double max = Collections.max(data);
				double min = Collections.min(data);
				
				JLabel xLabel =  new JLabel("" + round((plfCalcYLabel(max, min, (i+1) * yMeassureStep, yLength)),2));
				int lWidth = xLabel.getPreferredSize().width;
				int lHeight = xLabel.getPreferredSize().height;
				xLabel.setBounds(WIDTH_OFFSET - lWidth,(int) (HEIGHT_OFFSET + yMeassureStep * (i+1)) -lHeight,lWidth,lHeight);
				
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String dateString = dateFormat.format(timeline.get(timeline.size() / 10 * i));
				JLabel yLabel =  new JLabel(dateString);
				lWidth = yLabel.getPreferredSize().width;
				lHeight = yLabel.getPreferredSize().height;
				yLabel.setBounds(WIDTH_OFFSET + (int) (i * xMeassureStep),(int) (HEIGHT_OFFSET + yLength),lWidth,lHeight);
				
				add(xLabel);
				add(yLabel);
				
				// display last course
				JLabel lastCourse = new JLabel("Last course:" + data.get(data.size()-1));
				lWidth = lastCourse.getPreferredSize().width;
				lHeight = lastCourse.getPreferredSize().height;
				lastCourse.setBounds(getWidth() - WIDTH_OFFSET-lWidth,HEIGHT_OFFSET,lWidth,lHeight);
				add(lastCourse);
			}
			g2.draw(yMeassure);
			g2.draw(xMeassure);
		}

		if(plot) {
			switch(plotOption) {
				case 't':
					plotLineForm(g2, xLength,yLength);
					break;
				case 'c':
					plotConnectedForm(g2, xLength, yLength);
					break;
			}
		}
	}
	
	/**
	 * plot with line for each data point; length of each line symbolizes development on specified point of time
	 * short name: pLF
	 * @param g
	 * @param xLength
	 * @param yLength
	 */
	private void plotLineForm(Graphics2D g, double xLength, double yLength) {
		double max = Collections.max(data);
		double min = Collections.min(data);
		int numSteps;
		if(timeline.size() <= data.size ()) numSteps = timeline.size();
		else numSteps = data.size();
		
		int labelDist = numSteps /10;
		
		for(int i = 1; i < numSteps; ++i) {
			
			double start = plfCalcRelPos(max,min,data.get(i-1), yLength) + HEIGHT_OFFSET;
			double end = plfCalcRelPos(max,min,data.get(i), yLength) + HEIGHT_OFFSET;
			double tickWidth = (double) i / numSteps * xLength + WIDTH_OFFSET;
			Line2D tick = new Line2D.Double(tickWidth,start,tickWidth,end);
			
			if(data.get(i) == max || data.get(i) == min || i % labelDist == 0) {
				JLabel label = new JLabel("" + data.get(i));
				int lWidth = label.getPreferredSize().width;
				int lHeight = label.getPreferredSize().height;
				label.setBounds((int) tickWidth, (int) end, lWidth, lHeight);
				this.add(label);
				//System.out.println("" + data.get(i));
			}
			if(start < end) g.setColor(Color.GREEN);
			else g.setColor(Color.RED);
				
			g.draw(tick);
			
		}
		g.setColor(Color.BLACK);
	}
	
	/** plot graph with connected dots
	 * 
	 * @param g
	 * @param xLength
	 * @param yLength
	 */
	private void plotConnectedForm(Graphics2D g, double xLength, double yLength) {
		double max = Collections.max(data);
		double min = Collections.min(data);
		int numSteps;
		if(timeline.size() <= data.size ()) numSteps = timeline.size();
		else numSteps = data.size();
		
		int labelDist = numSteps /10;
		
		for(int i = 1; i < numSteps; ++i) {
			
			double start = plfCalcRelPos(max,min,data.get(i-1), yLength) + HEIGHT_OFFSET;
			double end = plfCalcRelPos(max,min,data.get(i), yLength) + HEIGHT_OFFSET;
			double tickWidth = (double) i / numSteps * xLength + WIDTH_OFFSET;
			Line2D tick = new Line2D.Double(tickWidth-1. / numSteps * xLength, start,tickWidth,end);
			
			if(data.get(i) == max || data.get(i) == min || i % labelDist == 0) {
				JLabel label = new JLabel("" + data.get(i));
				int lWidth = label.getPreferredSize().width;
				int lHeight = label.getPreferredSize().height;
				label.setBounds((int) tickWidth, (int) end, lWidth, lHeight);
				this.add(label);
				System.out.println("" + data.get(i));
			}
			g.draw(tick);
		}
	}
	
	/** calculate position of a point relative to the panel borders and the coordinate system from data value */
	private double plfCalcRelPos(double max, double min, double y, double yLength) {
		double yRel = (max - y)/(max  - min ) * 0.8 * yLength + 0.1 * yLength;  // add 10 % margin on top and bottom
		
		return yRel;
	}
	
	/** calulate the data value from the relative position of the panel and coordinate system */
	private double plfCalcYLabel(double max, double min, double yRel, double yLength) {
		double y = (-1) * (yRel - 0.1 * yLength) * (max - min) / (0.8 * yLength) + max;
		return y;
	}
	
	/** feed the requested data to a CurrencyGraph object */
	public void feedData(boolean plotFeed, ArrayList <Date> timelineFeed, ArrayList <Double> dataFeed, String cur) {
		plot = plotFeed;
		timeline = timelineFeed;
		data = dataFeed;
		currency = cur;
	}
	
	/** Sets the plot Option 
	 * Implemented later
	 * @param pO
	 */
	public void setPlotOption(char pO) {
		
	}
	
	/** Rounds double to certain number of digits 
	 * 
	 * @param value double value to round
	 * @param places number of digits after dot
	 * @return rounded number
	 */
	private static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	    BigDecimal bd = new BigDecimal(Double.toString(value));
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	private String currency;
	
	private ArrayList <Date> timeline;
	private ArrayList <Double> data;
	private boolean plot;
	private char plotOption;
}
