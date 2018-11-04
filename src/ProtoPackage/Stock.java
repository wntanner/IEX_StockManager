package ProtoPackage;

import java.text.DecimalFormat;

/**
 * Stock object keeps track of ticker symbol, open price, close price, latest
 * price, full name.
 * Knows how to make a String formatted for file writing
 * Knows how to take a String and convert it into a Stock
 * 
 * @author William
 *
 */
public class Stock {

	// number of attributes in the class 
	public static int NUM_ATTRIBUTES = 5;

	/**
	 * Given a formatted string of stock 
	 * 
	 * 	Line 1 : ticker symbol
	 * 	Line 2 : company name
	 *  Line 3 : open price
	 *  Line 4 : close price
	 *  Line 5 : latest price
	 *  
	 *  Makes and returns the Stock object
	 *  
	 * @param strstock
	 * @return
	 */
	public static Stock parseStock(String strstock) {
		Stock toMake = null;

		String[] parts = strstock.split(System.getProperty("line.separator"));
		// symbol
		String sym = parts[0];
		toMake = new Stock(sym);
		// name
		String nm = parts[1];
		toMake.setName(nm);
		//open price
		String opn = parts[2];
		toMake.setOpen(Double.parseDouble(opn));
		// close price
		String cls = parts[3];
		toMake.setClose(Double.parseDouble(cls));
		// latest price
		String lts = parts[4];
		toMake.setLatest(Double.parseDouble(lts));

		return toMake;

	}

	private String symbol;
	private String fullName;
	private Double open;

	private Double close;
	private Double latest;
	
	// for keeping us to 2 decimal places including trailing 0's
	private DecimalFormat df;

	public Stock(String sym) {
		
		this.symbol = sym;
		// TODO figure out a better way to write this
		df = new DecimalFormat("#.00"); 

		this.close = null;
		this.open = null;
		this.latest = null;
	}
	
	public Stock(String sym, double open) {

		this.symbol = sym.toUpperCase().trim();
		this.open = open;

	}

	public String toString() {
		return this.fullName;
	}

	public String getName() {
		return this.fullName;
	}

	public void setName(String name) {
		this.fullName = name;
	}

	public void setOpen(double newopen) {
		this.open = newopen;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public String getFormatOpen() {
		return df.format(this.open);
	}

	public String getFormatClose() {
		return df.format(this.close);
	}

	public Double getOpen() {
		return this.open;
	}

	public Double getClose() {
		return this.close;
	}

	public void setClose(double nclose) {
		this.close = nclose;
	}

	public void setLatest(double nlate) {
		this.latest = nlate;
	}

	public String getFormatLatest() {
		return this.df.format(this.latest);
	}
	
	/**
	 * Writes out the stock in format for the StockFiler to read.
	 * Each attribute on an individual line ending in "END"
	 * @return
	 */
	public String writeOut() {
		StringBuffer build = new StringBuffer();

		build.append(this.symbol + System.getProperty("line.separator"));
		build.append(this.fullName + System.getProperty("line.separator"));
		build.append(this.open + System.getProperty("line.separator"));
		build.append(this.close + System.getProperty("line.separator"));
		build.append(this.latest + System.getProperty("line.separator"));
		build.append("END" + System.getProperty("line.separator"));

		return build.toString();
	}

}
