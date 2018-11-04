package ProtoPackage;
// TODO duplicate stocks tell user that the stock is unknown
// check for duplicates before calling the API
/**
 * Stocks program that keeps track of a dynamic list of stocks
 * in a file system and displays messages about them.
 * 
 * Objects in use:
 * 	ProtoDriver
 * 	ProtoFrame
 * 	ProtoController
 * 	IEX_Caller
 * 	StockFiler
 * 	Stock
 * 
 * @author William
 *
 */
public class ProtoDriver {

	public static void main(String[] args) {

		ProtoFrame pframe = new ProtoFrame();
		
		pframe.setVisible(true);
		
	}

}
