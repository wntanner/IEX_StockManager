package unusedTrash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;

import ProtoPackage.Stock;

/**
 * Manages all of the logic of making stocks, finding open prices, and stuff. --
 * Data about all the stocks.
 * 
 * @author William
 *
 */
public class Controller {

	public static final String IEX_ATTRIBUTION = " Data provided for free by IEX. View IEX's Terms of Use at https://iextrading.com/api-exhibit-a/ ... ";
	public static final String UNKNOWN_SYMBOL_ERROR = "Unknown symbol"; // IEX's unknown symbol message

	// URL to call API from
	public static final String BASE_URL = "https://api.iextrading.com/1.0/stock/"; // + /{Symbol}/book"

	public static final String OPEN_MATCH = "\"open\""; // attribute for the opening price of the stock
	
	// file path of the file we write to
	public static final String FILEPATH = System.getProperty("user.home") + File.separator + "Stock_Info";
	// System.getProperty("user.home") - also works as a good standard file path

	public static final double BAD_CALL = -1.0; // marker for if we fail to get the open price of the stock from bad API
												// call etc.

	
	private final String[] STOCK_SYMBOLS = { "msft", "unh", "ba", "pg", "amd", "wmt", "tsla", "sbux", "ge", "goog",
			"amzn", "dpz" };
	
	
	
	private ArrayList<Stock> stocks; // list of target stocks
	private FileInterpreter finter;
	
	public Controller(boolean doFileWork) {

		stocks = new ArrayList<Stock>();
		finter = new FileInterpreter(doFileWork);
		
	}
	
	/**
	 * adds the fixed fixed list of stocks to stocklist and writes message to the file
	 */
	public void addFixedListToStocksList() {
	
		boolean madeStock = false;
		
		for (String cur : this.STOCK_SYMBOLS) {
			
			madeStock = this.makeAndAddStock(cur); // make and add stock to list
			
			if (madeStock != true) {
				// TODO inform the user that a particular stock did not go through because not unique or bad call
				
			}

		}
		
	}
	
	public void writeStockMessage() {
		this.finter.writeStockMessage(this.getStocksMessage());
	}
	


	/**
	 * Checks back with IEX API and sets the stocks' open prices to the most recent
	 * open price.
	 * 
	 */
	public void refreshOpenPrices() {

		if (!stocks.isEmpty()) {

			for (Stock s : stocks) {

				s.setOpen(this.getOpenPrice(s.getSymbol()));

			}

		}

	}

	/**
	 * Gets the open prices for all the stocks in the list writes the ticker symbols
	 * and open prices to the target file.
	 * 
	 * @return
	 */
	public String getStocksMessage() {
		
		StringBuffer rtn = new StringBuffer();
		
		Calendar c = Calendar.getInstance();
		
		int mon = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int year = c.get(Calendar.YEAR);
		
		StringBuffer dateForm = new StringBuffer();
		dateForm.append(day + "/" + mon + "/" + year);

		rtn.append("... Welcome to Austin College " + dateForm.toString() + "...\n"); // + IEX_ATTRIBUTION);

		for (Stock s : this.stocks) {

			rtn.append(s.getSymbol() + " " + s.getOpen() + "   \n");
			
		}

		return rtn.toString();

	}

	/**
	 * Gets the most recent open price of given stock symbol
	 * 
	 * @param symbol
	 * @return
	 */
	private double getOpenPrice(String symbol) {

		double rtn = BAD_CALL;

		String url_string = Controller.BASE_URL + symbol + "/book";
		BufferedReader buffr = null;

		try {

			URL url = new URL(url_string);

			URLConnection con = url.openConnection();

			// JsonReader jread;
			InputStreamReader isr = new InputStreamReader(con.getInputStream());

			buffr = new BufferedReader(isr);

			String line1 = buffr.readLine();

			if (line1.equals(Controller.UNKNOWN_SYMBOL_ERROR)) {
				System.err.println(symbol + " is not a known symbol in the IEX API");
				return rtn;
			}

			/*
			 * TODO TODO TODO TODO TODO TODO TODO TODO This is a really immature and weak
			 * way to get the open price -- we treat the JSON as a normal file, stick the
			 * first 2 lines of the JSON together, split on commas, find the substring where
			 * the first chunk of letters matches exactly with "open" , which is the
			 * attribute name of the open price then get the following digits.
			 * 
			 * This needs to be improved by learning how to parse JSON with GSON or Jackson
			 * or something.
			 * 
			 */

			String line12 = line1 + buffr.readLine();
			String line123 = line12 + buffr.readLine();
			String[] splitLines = line123.split(",");

			for (int i = 0; i < splitLines.length - 1; i++) {
				if (splitLines[i].substring(0, OPEN_MATCH.length()).equals(OPEN_MATCH)) {

					String openprice = splitLines[i].split(":")[1]; // TODO
					rtn = Double.parseDouble(openprice);
				}
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (FileNotFoundException e) {

			System.err.println(symbol + " is not supported by IEX");

			e.printStackTrace();
			return BAD_CALL;
		} catch (IOException e) {
			//
			e.printStackTrace();
		}

		return rtn;
	}

	/**
	 * Makes and Adds stock to list.
	 * 
	 * Makes a stock with the given symbol and Adds the stock to the list. Calls the IEX API and gets the open
	 * price for the given stock TODO learn how to work with JSON and just get the
	 * "open" attribute directly from the IEX API call
	 * 
	 * @param symbol
	 *            Stock ticker symbol
	 */
	public boolean makeAndAddStock(String symbol) {

		double opendbl = this.getOpenPrice(symbol);

		if (opendbl == BAD_CALL) {
			return false;
		}
		
		for(Stock cur : this.stocks) {
			if(cur.getSymbol().equals(symbol)) {
				return false;
			}
		}

		Stock toAdd = new Stock(symbol, opendbl);
		
		
		
		
		
		this.stocks.add(toAdd);

		return true;
	}

}
