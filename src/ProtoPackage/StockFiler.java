package ProtoPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.DefaultListModel;

/**
 * Controls the File system for storing and retrieving data about the stocks in
 * the list.
 * 
 * @author William
 *
 */
public class StockFiler {

	// file path where we store stuff
	public static final String FILEPATH = System.getProperty("user.dir") + File.separator + "Proto_StocksFile"
			+ File.separator + "Proto_Stocks.txt";
	// System.getProperty("user.home") - also works as a good standard file path
	// to check if file is empty
	public static final String EMPTY_FILE = "empty";

	private File target;

	public StockFiler() {
		this.target = new File(FILEPATH);
		FileWriter fw = null;
		try {
			if (!target.exists()) {
				target.getParentFile().mkdirs();
				target.createNewFile();
				fw = new FileWriter(target);
				fw.write(EMPTY_FILE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public File getFile() {
		return this.target;
	}

	/**
	 * Looks for the target : File and sets up a DefaultListModel full of stocks in
	 * the file
	 * 
	 * @return
	 */
	public DefaultListModel<Stock> startup() {
		DefaultListModel<Stock> toRtn = new DefaultListModel<Stock>();
		// look for file
		this.parseStocksAndFill(toRtn);

		return toRtn;
	}

	/**
	 * Fills up a DefaultListModel with stocks in the target : File
	 * 
	 * @param toFill
	 */
	private void parseStocksAndFill(DefaultListModel<Stock> toFill) {

		BufferedReader buffr = null;

		try {
			buffr = new BufferedReader(new FileReader(this.target));
			String ln = buffr.readLine();
			if (ln == null) {
				buffr.close();
				return;
			}

			if (ln.equals(EMPTY_FILE)) {
				buffr.close();
				return;
			}

			/*
			 * Read in stocks until we reach two END markers in a row
			 */
			while (!ln.equals("END")) {
				StringBuffer stockstr = new StringBuffer();

				while (!ln.contains("END")) {
					// build up a string with stock information
					stockstr.append(ln);
					stockstr.append(System.getProperty("line.separator"));
					ln = buffr.readLine();

				}

				// create a stock with the string and add it to the list
				toFill.addElement(Stock.parseStock(stockstr.toString()));
				ln = buffr.readLine();

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Writes out all information about the stocks list to the target : File
	 * 
	 * @param stocks
	 */
	public void save(DefaultListModel<Stock> stocks) {

		BufferedWriter buffr = null;
		FileWriter fr;
		try {
			fr = new FileWriter(this.target);
			buffr = new BufferedWriter(fr);

			for (int i = 0; i < stocks.getSize(); i++) {
				System.out.print(stocks.getElementAt(i).writeOut());
				// write the stock out to file
				buffr.write(stocks.get(i).writeOut());

			}

			buffr.write("END");

		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			if (buffr != null) {
				try {
					buffr.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}

	}

}
