package currencyTracker;
import java.io.*;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.*;
import java.net.http.HttpResponse.*;
import java.util.*;
import java.text.*;
import com.github.cliftonlabs.json_simple.*;

import currencyTracker.apiclasses.alphavantage;
import currencyTracker.apiclasses.exchangerateapi;



public class CurrencyDataRequest implements StandardVariables{
	/** Constructor of the CurrencyDataRequest class 
	 * 
	 * @param type For a request of a single date use param "1", for multiple dates use "2"
	 * @param options extension to add to the URL
	 */
	public CurrencyDataRequest(String inputSource, String options, int type)  { 
		dataType = type;
		source = sourceChooser(inputSource); // source specifies the data source; if source = 'e', data source is exchangeratesapi.io
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url+options)).build();
		HttpResponse<String> response;
		try {
			response = client.send(request, BodyHandlers.ofString());
			switch(source) {
			case 'e': 
				exrateapi = new exchangerateapi(response);
				break;
			case 'a':
				alphavantage alphaApi = new alphavantage(response, options);
				
				break;
			}
		} catch (IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
	
	/* BEGIN APIEXCHANGE.IO
	 * ----------------------------------------------------------------------------------------------
	 */
	
	/* END APIEXCHANGE.IO
	 * ----------------------------------------------------------------------------------------------
	 */
	/* BEGIN ALPHAVANTAGE.IO
	 * ----------------------------------------------------------------------------------------------
	 */

	
	/* END ALPHAVANTAGE.IO
	 * ----------------------------------------------------------------------------------------------
	 */
	public ArrayList<Double> getData(String cur){
		ArrayList <Double> data;
		switch(source) {
		/* APIEXCHANGE.IO */
		case 'e':
			data = exrateapi.getData(cur);
			break;
		case 'a':
			data = alphavantage.getData(cur);
			break;
		default:
			data = null;
			break;
		}
		return data;
	}

	public ArrayList<Date> getTimeLine(){
		ArrayList <Date> timeLine;
		switch(source) {
		case 'e':
			timeLine = exrateapi.getTimeLine();
			break;
		case 'a':
			timeLine = alphavantage.getTimeLine();
			break;
		default:
			timeLine = null;
			break;
		}
		return timeLine;
	}
	
	/* public String ratesToString() {
		String result = "";
		for(Iterator it = rates.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			result += key + ": " + rates.get(key) + "; ";
		}
		return result;
	} */
	
	private char sourceChooser(String inputSource) {
		if(inputSource.equals("exchangerateapi.io")) {
			url =  "https://api.exchangeratesapi.io/";
			return 'e';
		}
		else if(inputSource.equals("alphavantage.co")) {
			url = "https://www.alphavantage.co/";
			return 'a';
					
		}
		else {
			url = "";
			System.out.println("Unknown input");
			return '0';
		}
	}
	
	

	private exchangerateapi exrateapi;
	private String base;
	private String dateSingle;
	private String url;
	private int dataType;
	private int timestamp; // for future use
	private int statCode;
	private char source;



}
