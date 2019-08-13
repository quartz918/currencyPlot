package currencyTracker;
import java.io.*;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.*;
import java.net.http.HttpResponse.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.github.cliftonlabs.json_simple.*;



public class CurrencyDataRequest implements StandardVariables{
	/** Constructor of the CurrencyDataRequest class 
	 * 
	 * @param type For a request of a single date use param "single", for multiple dates use "multi"
	 * @param options extension to add to the URL
	 */
	public CurrencyDataRequest(String options, String type)  {
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL+options)).build();
		HttpResponse<String> response;
		try {
			response = client.send(request, BodyHandlers.ofString());
			if(type.equals("single")){
				parseDataSingle(response);
				dateType = 0;
			}
			else if(type.equals("multi")){
				parseDataMulti(response);
				dateType = 1;
			}
			else {
				System.out.println("Use single or multi as constructor arguments");
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			statCode = 9999;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			statCode = 9999;
		}	
		
	}
	
	/** Parses an http request for a single date
	 * 
	 * @param response Http request to parse
	 */
	private void parseDataSingle(HttpResponse<String> response) {
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
	private void parseDataMulti(HttpResponse<String> response) {
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
	
	/** Get the rates on a specific date for a mulit date request 
	 * 
	 * @param date the date of interest
	 * @return true if data for the specific date exists, false otherwise
	 */
	private boolean setRatesOnDate(String date) {
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
	
	private HashMap<String,BigDecimal> castToHashMapStringBigDec(JsonObject jsonRates){
		rates = new HashMap<String,BigDecimal>();
		for(Iterator it = jsonRates.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			rates.put(key, (BigDecimal) jsonRates.get( key));
		}
		return rates;
	}
		
	
	
	public HashMap<String,BigDecimal> getRates(){ 
		return rates;
	}
	
	public HashMap<String,BigDecimal> getRates(String date){ 
		if(setRatesOnDate(date)) { 
			dateSingle = date;
			return rates;
		}
		else {
			System.out.println("No data for " + date);
			return null;
		}
	}
	
	/** Get maximum value of currency in the requested time frame
	 * 
	 * @param cur selected currency
	 * @return
	 */
	public double getMax(String cur) {
		double max = 0;
		for(Iterator it = jsonDates.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			JsonObject temp = (JsonObject) jsonDates.get(key);
			if(temp.containsKey(cur)) {
				if(max < ((BigDecimal) temp.get(cur)).doubleValue()) {
					max = ((BigDecimal) temp.get(cur)).doubleValue();
				}
			}
		}
		return max;
	}
	
	/** Get number of requested datasets
	 * 
	 * @return
	 */
	public int getNumDataSets() {
		int result = jsonDates.size();
		return result;
	}
	
	public String ratesToString() {
		String result = "";
		for(Iterator it = rates.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			result += key + ": " + rates.get(key) + "; ";
		}
		return result;
	}
	private JsonObject jsonDates;
	private HashMap<String,BigDecimal> rates;
	private String base;
	private String dateSingle;

	private int dateType;
	private int timestamp; // for future use

	private int statCode;

}
