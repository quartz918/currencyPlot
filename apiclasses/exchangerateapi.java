package currencyTracker.apiclasses;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;


public class exchangerateapi {
	public exchangerateapi(HttpResponse<String> response) {
		
		if(dataType == 1){
			parseDataSingleE(response);	
		}
		else if(dataType == 2){
			parseDataMultiE(response);
		}
		else {
			System.out.println("Use single or multi as constructor arguments");
			
		}
	}


/** Parses an http request for a single date
 * 
 * @param response Http request to parse
 */
private void parseDataSingleE(HttpResponse<String> response) {
	String body = response.body();
	JsonObject jsonObject;
	JsonObject jsonRates;

	try {
		jsonObject = (JsonObject) Jsoner.deserialize(body);
		jsonRates = (JsonObject) jsonObject.get("rates");
		rates = castToHashMapStringBigDec(jsonRates);
		dateSingle = (String) jsonObject.get("date");
	} catch (JsonException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

	/** Parses an http request for multiple dates
	 * 
	 * @param response Http request to parse
	 */
	private void parseDataMultiE(HttpResponse<String> response) {
		String body = response.body();
		JsonObject jsonObject;
		
		try {
			jsonObject = (JsonObject) Jsoner.deserialize(body);
			jsonDates = (JsonObject) jsonObject.get("rates");		
		} catch (JsonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private HashMap<String,BigDecimal> castToHashMapStringBigDec(JsonObject jsonRates){
		rates = new HashMap<String,BigDecimal>();
		for(Iterator it = jsonRates.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			rates.put(key, (BigDecimal) jsonRates.get( key));
		}
		return rates;
	}

	private boolean setRatesOnDate(String date ) {
		if(date.equals(null)) {
			System.out.println("Error: no date set");
			return false;
		}
		else {
			JsonObject jsonRates;
			if(jsonDates.containsKey(date)) {			
				jsonRates = (JsonObject) jsonDates.get(date);
				rates = castToHashMapStringBigDec(jsonRates);
				return true;
			}
			else
				return false;	
		}
	}
	
	public  ArrayList<Double> getData(String cur) {
		ArrayList <Double> data = new ArrayList<Double>();
		if(dataType == 1) {
			data.add(rates.get(cur).doubleValue());
		} 
		else if(dataType == 2) {
			Object[] dates = jsonDates.keySet().toArray();
			Arrays.sort(dates);
			for(Object key : dates) {
				setRatesOnDate((String) key);
				data.add(rates.get(cur).doubleValue());
			}
		}
		else return null;
		return data;
	}
	
	public ArrayList<Date> getTimeLine(){
		ArrayList <Date> timeLine = new ArrayList<Date>();
		if(dataType == 1) {
			try {
				timeLine.add(new SimpleDateFormat("yyyy-MM-dd").parse(dateSingle));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.out.println("Error while converting date");
				e.printStackTrace();
			}
		}
		else if(dataType == 2) {
			Object[] dates = jsonDates.keySet().toArray();
			Arrays.sort(dates);
			for(Object key : dates) {
				try {
					timeLine.add(new SimpleDateFormat("yyyy-MM-dd").parse((String) key));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					System.out.println("Error while converting date");
					e.printStackTrace();
				}
			}
		}
		else return null;
		return timeLine;
	}
	private  int dataType = 2;  // For plotting historic data; change for implementation for single day
	private JsonObject jsonDates;
	private HashMap<String,BigDecimal> rates;

	private String base;
	private String dateSingle;
}
