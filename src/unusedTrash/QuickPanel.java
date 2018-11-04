package unusedTrash;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Labels and displays stock message in a text area.
 * 
 * @author William
 *
 */
public class QuickPanel extends JPanel {

	private final Font FONT = new Font("Courier", Font.PLAIN, 25);;
	private JTextArea txtMessage;
	private Controller c;

	public QuickPanel() {

		setLayout(new BorderLayout(0, 0));

		JLabel lblMessage = new JLabel("Message:");
		lblMessage.setFont(FONT);
		add(lblMessage, BorderLayout.NORTH);

		txtMessage = new JTextArea();
		txtMessage.setFont(FONT);
		// add(txtMessage);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(txtMessage);
		this.add(scrollPane);
		
		
		
		c = new Controller(false);

		// c.addFixedListToStocksList();

		String stockmsg = c.getStocksMessage();

		txtMessage.setLineWrap(true);

		this.txtMessage.setText(stockmsg);
		this.txtMessage.setEditable(false);
	}

	public void refreshMessage() {
		this.txtMessage.setEditable(true);
		this.txtMessage.setText(c.getStocksMessage());
	}

	public Controller getController() {
		return this.c;
	}

}
