package ProtoPackage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Communicates with the IEX API.
 * 
 * @author William
 *
 */
public class IEX_Caller {

	// http://financials.morningstar.com/ajax/ReportProcess4CSV.html?t=TWTR&reportType=is&period=12&dataType=A&order=asc&columnYear=5&number=3
	// TODO switch to a morningstar caller, read as csv ??? morningstar has
	// different data ...

	/*
	 * Constants
	 */
	public static final String IEX_ATTRIBUTION = "Data provided for free by IEX... "; // View IEX's Terms of Use at https://iextrading.com/api-exhibit-a/ ... ";
	public static final String UNKNOWN_SYMBOL_ERROR = "Unknown symbol"; // IEX's unknown symbol message
	public static final String UNFINISHED_BUILD_ERROR = "Probelem with software - incomplete reading of stock information";
	public static final String COMPLETED_CALL = "good";
	public static final String ERROR = "error";
	// URL to call API from
	public static final String BASE_URL = "https://api.iextrading.com/1.0/stock/"; // + "/{Symbol}/book"

	/**
	 * Given a stock symbol, creates and returns a String
	 * for the URL
	 * 
	 * @param sym
	 * @return
	 */
	public static String make_URL(String sym) {
		return BASE_URL + sym + "/book";
	}
	
	/*
	 * Strings that correspond to various attributes in the JSON file
	 * which IEX returns. TODO learn GSON
	 */
	public static final String OPEN_MATCH = "\"open\""; // attribute for daily opening price of stock
	public static final String CLOSE_MATCH = "\"close\""; // attribute for daily closing price of stock
	public static final String NAME_MATCH = "\"companyName\"";
	public static final String LATEST_PRICE_MATCH = "latestPrice";


	public IEX_Caller() {
	}

	/**
	 * Given a Stock : s, calls the IEX API
	 * Fills the Stock's attributes with whatever IEX returned
	 * 
	 * @param s
	 * @return
	 */
	public String callAndFill(Stock s) {

		String sym = s.getSymbol();

		String url_path = IEX_Caller.make_URL(sym);

		BufferedReader buffr = null;
		try {
			/*
			 * Connect to the API and read in the input
			 */
			URL url = new URL(url_path);
			System.out.println("url is: " + url.getPath());
			URLConnection con = url.openConnection();
			System.out.println(con.toString());
			InputStreamReader isr = new InputStreamReader(con.getInputStream());

			buffr = new BufferedReader(isr);
			this.fillStock(buffr, s);

		}
		
		catch (MalformedURLException e) {
			System.err.println("bad url - IEX_Caller.callAndFillStock");
			e.printStackTrace();
			return ERROR;
		} catch (FileNotFoundException e) {
			e.printStackTrace();

			return IEX_Caller.UNKNOWN_SYMBOL_ERROR;
		} catch (IOException e) {
			
			e.printStackTrace();
			return ERROR + " with IO";
		} finally {
			if (buffr != null) {
				try {
					buffr.close();
				} catch (IOException e) {
					System.err.println("everything is broken");
					e.printStackTrace();
				}
			}
		}
		/*
		 * Close is a tricky thing to get, but we solved.
		 * We can remove this check.
		 */
		if (s.getOpen() == null || s.getClose() == null)
			return IEX_Caller.UNFINISHED_BUILD_ERROR;

		// reader.convertLinesToString(numlines, stream);

		return IEX_Caller.COMPLETED_CALL;

	}

	/**
	 * Given a BufferedReader : buffr holding onto an InputStream 
	 * from an IEX API call and a Stock, 
	 * reads in information from buffr and fills up the Stock
	 * with the stuff.
	 * 
	 * @param buffr
	 * @param s
	 * @return
	 * @throws IOException
	 */
	public boolean fillStock(BufferedReader buffr, Stock s) throws IOException {
		System.out.print("filling stock: " + s.getSymbol());

		String ln = buffr.readLine();

		if (ln.equals(IEX_Caller.UNKNOWN_SYMBOL_ERROR))
			return false;
		
		// grab the first five lines of the file and split on comma ...
		this.padWithLines(buffr, ln, 5);
		String[] splitlines = ln.split(",");
		
		/*
		 * ... so that we can start to search for matches
		 * with the attributes we want
		 */
		for (int i = 0; i < splitlines.length - 1; i++) {
			String cur = splitlines[i];
//			System.out.println("current attribute checking is: " + cur);

			if (cur.contains(NAME_MATCH)) {
				s.setName(cur.split(":")[1]);
			}
			if (cur.contains(OPEN_MATCH)) {
				s.setOpen(Double.parseDouble(cur.split(":")[1]));
			}

			if (cur.contains(LATEST_PRICE_MATCH)) {
				s.setLatest(Double.parseDouble(cur.split(":")[1]));
				break;
			}

			if (cur.contains(CLOSE_MATCH) && !cur.contains("calculation")) {
				s.setClose(Double.parseDouble(cur.split(":")[1]));
			}

		}
		return true;
	}
	/**
	 * Given a BufferedReader and String, sticks num number of lines
	 * onto the String
	 * 
	 * @param buffr
	 * @param toPad
	 * @param num
	 * @throws IOException
	 */
	private void padWithLines(BufferedReader buffr, String toPad, int num) throws IOException {
		for (int i = 0; i < num; i++) {
			toPad += buffr.readLine();
		}
	}

}
