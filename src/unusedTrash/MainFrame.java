package unusedTrash;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

/**
 * Displays the MainPanel or QuickPanel, controls intial frame size, exits on close.
 * 
 * @author William
 *
 */
public class MainFrame extends JFrame {
	
	public static Dimension F_DIM = new Dimension(1000, 500);
	
	private QuickPanel mp;
	
	public MainFrame() {
		
		this.setPreferredSize(F_DIM);
		this.setMinimumSize(F_DIM);
		this.setMaximumSize(F_DIM);
		
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		
		mp = new QuickPanel();
		getContentPane().add(mp);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblStocksInList = new JLabel("Stocks in List: ");
		panel.add(lblStocksInList, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public QuickPanel getMainPanel() {
		return this.mp;
	}
	

}
