package ProtoPackage;

import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Calendar;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

/**
 * Manages all of the objects necessary for the stock program.
 * 
 * @author William
 *
 */
public class ProtoController {
	// behaviors for the display options
	public static final String[] DISPLAY_OPTIONS = new String[] { "Closing Price", "Open Price", "Latest Price" };
	public static int CLOSE = 0; // constant for closing the window
	
	
	private boolean isSaved;
	private JList<Stock> stockList;
	private DefaultListModel<Stock> stockModel; // list of stocks
	private IEX_Caller caller;
	private StockFiler filer;

	public ProtoController() {
		filer = new StockFiler();

		this.isSaved = true;
		this.stockModel = filer.startup(); // sets up the model of stocks
		
		this.stockList = new JList<Stock>(stockModel);
		this.stockList.setToolTipText("");
		// display a tool tip over the moused-over stock in the list
		stockList.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent arg0) {

			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				displayToolTip(arg0);
			}

		});

		this.caller = new IEX_Caller();

	}

	public JList<Stock> getStockList() {
		return this.stockList;
	}
	
	/**
	 * Gets the mouse location
	 * Converts location to index
	 * Displays the stock ticker symbol over the stock
	 * that the mouse is hovering over
	 * 
	 * @param e : MouseEvent to getPoint to convert point location to index
	 */
	public void displayToolTip(MouseEvent e) {
		int index = this.stockList.locationToIndex(e.getPoint());
		if (index == -1) {
			this.stockList.setToolTipText("");
			return;
		}
		Stock ss = this.stockModel.get(index);
		this.stockList.setToolTipText(ss.getSymbol());

	}
	
	/**
	 * Removes the selected stock in the list from the model
	 */
	public void removeSelectedStock() {
		this.isSaved = false;
		int index = this.stockList.getSelectedIndex();

		if (index == -1)
			return;

		System.out.println("removing: " + this.stockList.getSelectedValue().toString());

		this.stockModel.remove(index);

	}
	
	/**
	 * Opens up a JOptionPane on the given Frame
	 * Prompts user input for a stock ticker symbol
	 * Adds the stock to the list
	 * 
	 * @param parent : Frame to get user input about the stock
	 */
	public void addStock(Frame parent) {
		this.isSaved = false;
		String ticker = JOptionPane.showInputDialog(parent, "Enter a stock ticker symbol: ");
		if (ticker == null)
			return;
		
		// format the symbol
		ticker = ticker.trim().toUpperCase();
		System.out.println("ticker is: " + ticker);
		Stock toAdd = new Stock(ticker);

		/*
		 * make sure symbol is unique by making sure no
		 * other stock in the list has the same ticker symbol
		 */
		boolean unique = true;
		for (int i = 0; i < this.stockModel.getSize(); i++) {
			if (this.stockModel.get(i).getSymbol().equalsIgnoreCase(ticker)) {
				JOptionPane.showMessageDialog(parent, ticker + " is already in your list", "Error",
						JOptionPane.WARNING_MESSAGE);
				unique = false;
				return;
			}
		}
		
		// make sure the symbol is valid
		boolean valid = this.fillStock(toAdd, parent);
		if (!valid)
			return;
		
		System.out.println("Stock toAdd known-ness is: " + valid);
		
	
		
		// add the totally good stock to the model
		if (unique && valid)
			this.stockModel.addElement(toAdd);

	}

	/**
	 * Fills a given stock with information from IEX
	 * if the symbol is known and everything works right, return true
	 * 
	 * if the symbols is unknown or something bad happens, return false
	 * 
	 * @param toFill : Stock to fill with info
	 * @param par : Frame to display error message if needed
	 * @return : true if successfully filled or false if failed
	 */
	private boolean fillStock(Stock toFill, Frame par) {
		this.isSaved = false;
		boolean isValid = false;
		
		String msg = caller.callAndFill(toFill);
		
		if (!msg.equals(IEX_Caller.COMPLETED_CALL)) {
			JOptionPane.showMessageDialog(par, msg, "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			isValid = true;
		}

		return isValid;

	}
	
	/**
	 * Given the type of message to display,
	 * Formats and returns the message to display
	 * Refreshes the stock prices before displaying
	 * 
	 * @param type : Constant determining which type of price to display
	 * @return : final message
	 */
	public String refreshMessage(int type) {
		if(type == JOptionPane.CLOSED_OPTION) {
			return "";
		}
		
		this.refreshStocks(); // TODO check this
		
		StringBuffer rtn = new StringBuffer();
		
		Calendar c = Calendar.getInstance();

		int mon = c.get(Calendar.MONTH) + 1; // January starts at 0
		int day = c.get(Calendar.DAY_OF_MONTH);
		int year = c.get(Calendar.YEAR);

		StringBuffer dateForm = new StringBuffer();
		dateForm.append(mon + "-" + day + "-" + year);
		// keep 3 spaces between each thing
		rtn.append("Welcome to Austin College " + dateForm.toString() + " " + IEX_Caller.IEX_ATTRIBUTION + "\n");
		String fill = new String();

		for (int i = 0; i < this.stockModel.getSize(); i++) {
			Stock s = this.stockModel.get(i);
			switch (type) {
			case 0:
				fill = s.getFormatClose();
				break;
			case 1:
				fill = s.getFormatOpen();
				break;
			case 2:
				fill = s.getFormatLatest();
				break;
			default:
				return "nothing good happened at - ProtoController.refreshMessage()";
			}
			
			// keep 3 spaces in between each stock and on new lines
			rtn.append(s.getSymbol() + " " + fill + "   \n");

		}
		
		// keep an extra new line at the end
		return rtn.toString() + System.getProperty("line.separator"); // + "END";

	}

	/**
	 * Given the Frame the user is trying to close,
	 * prompt the user with a reminder to save their stocks
	 * before closing
	 * 
	 * @param par
	 * @return
	 */
	public int attemptClose(Frame par) {
		
		if(this.isSaved)
			return ProtoController.CLOSE;
		
		return JOptionPane.showConfirmDialog(par, "You did not save your work. \n Do you want to close without saving?", "Confirm Close",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	}
	
	/**
	 * Writes all the stocks to the file formatted
	 */
	public void saveAll() {
		this.isSaved = true;
		this.filer.save(this.stockModel);
	}
	
	/**
	 * Refresh all of the stocks' price data
	 * from IEX then saves the data
	 */
	public void refreshStocks() {
		this.isSaved = false;
		// TODO this throws a null pointer if you click refresh first
		for (int i = 0; i < this.stockModel.getSize(); i++) {
			this.caller.callAndFill(this.stockModel.get(i));
		}

	}
	
	/**
	 * Display some options -- TODO finish this
	 * TODO implement a boolean to keep track of changes to the list
	 * and only prompt to save if the user needs to save the list
	 * @param par
	 */
	public void optionsClicked(Frame par) {
		JOptionPane.showMessageDialog(par, "<" + this.filer.getFile().getAbsolutePath() + "> is the file path", "Options - Not Implemented Yet", JOptionPane.INFORMATION_MESSAGE);
	}

}
