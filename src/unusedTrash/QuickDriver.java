package unusedTrash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The main executable algorithm for simply writing a fixed list of stocks, their open
 * prices, an Austin College welcome message, and API attribution message and
 * displays it in a labeled text area.
 * 
 * @author William
 *
 */
public class QuickDriver {

	public static void main(String[] args) {
		MainFrame mf = new MainFrame();
		mf.setVisible(true);
		
		
		
		System.out.println(System.getProperty("user.dir"));
//		/z_IEX_Stock_Updater/
		ArrayList<String> cursymbs = new ArrayList<String>();
		
		File f = new File(System.getProperty("user.dir") + File.separator + "TickerMessage");
		BufferedReader br = null;
		try {
			
			FileReader fr = new FileReader(f);
			br = new BufferedReader(fr);
			
			String line = br.readLine();
			
			while(!line.equals("END")){
				
				String symb = line.split(" ")[0];
				mf.getMainPanel().getController().makeAndAddStock(symb);
				
				
				cursymbs.add(symb);
				System.out.print(symb + "---");
				line = br.readLine();
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
		mf.getMainPanel().refreshMessage();
		mf.repaint();
		
//		

		
	}

}
