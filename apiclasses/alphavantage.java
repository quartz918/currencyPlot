package currencyTracker.apiclasses;

import java.math.BigDecimal;
import java.net.http.HttpResponse;
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

public class alphavantage {
	public alphavantage(HttpResponse<String> response, String options) {
		parseDataAlphavantage(response, options);
	}
	private void parseDataAlphavantage(HttpResponse<String> response, String function) {
		String body = response.body();
		JsonObject jsonObject;
		
		try {
			jsonObject = (JsonObject) Jsoner.deserialize(body);
			jsonDates = (JsonObject) jsonObject.get("Time Series FX (Daily)");		
		} catch (JsonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private static boolean setRatesOnDateAlpha(String date) {
		if(date.equals(null)) {
			System.out.println("Error: no date set");
			return false;
		}
		else {
			JsonObject jsonRates;
			
			if(jsonDates.containsKey(date)) {			
				jsonRates = (JsonObject) jsonDates.get(date);
				rates = castToHashMapStringBigDecAlpha(jsonRates);
				return true;
			}
			else
				return false;	
		}
	}
	
	private static HashMap<String,BigDecimal> castToHashMapStringBigDecAlpha(JsonObject jsonRates){
		rates = new HashMap<String,BigDecimal>();
		for(Iterator it = jsonRates.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			BigDecimal temp = new BigDecimal((String) jsonRates.get( key));
			rates.put(key, temp);
		}
		return rates;
	}
	
	public static  ArrayList<Double> getData(String cur) {
		ArrayList <Double> data = new ArrayList<Double>();
		//if(jsonDates == null) System.out.println("NULL");
		Object[] dates = jsonDates.keySet().toArray();
		Arrays.sort(dates);
		for(Object key : dates) {
			setRatesOnDateAlpha((String) key);
			data.add(rates.get("4. close").doubleValue());
		}
		return data;
	}
	
	public static ArrayList<Date> getTimeLine(){
		ArrayList <Date> timeLine = new ArrayList<Date>();
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
		return timeLine;
	}
	
	private static JsonObject jsonDates;
	private static HashMap<String,BigDecimal> rates;
}
