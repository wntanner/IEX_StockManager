package ProtoPackage;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.FlowLayout;


/**
 * Displays Stock objects in a scroll pane and a message about them in a text
 * area in a scroll pane
 * 
 * @author William
 *
 */
public class ProtoFrame extends JFrame {

	// frame dimension
	public static final Dimension F_DIM = new Dimension(1200, 800);
	// big font mostly for labels
	public static final Font BIG_FONT = new Font("Courier", Font.PLAIN, 25);
	// smaller font for most buttons
	public static final Font BTN_FONT = new Font("Courier", Font.PLAIN, 20);
	// even smaller font
	// public static final Font LITTLE_FONT = new Font("Courier", Font.PLAIN, 15);

	private ProtoController protocon;
	private JList<Stock> stockview; // to hold list of stocks
	private JTextArea txtMsg; // to display the message

	public ProtoFrame() {
		super("Student Investment Stocks");
		this.setPreferredSize(F_DIM);
		this.setMinimumSize(F_DIM);
		this.setMaximumSize(F_DIM);

		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.protocon = new ProtoController();
		this.stockview = protocon.getStockList();

		/*
		 * Stocks list panel
		 */
		JPanel pnlStocksList = new JPanel();
		getContentPane().add(pnlStocksList, BorderLayout.WEST);
		pnlStocksList.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		pnlStocksList.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(this.stockview);
		scrollPane.getViewport().getView().setFont(ProtoFrame.BTN_FONT);

		JPanel pnlStockListBtns = new JPanel();
		pnlStocksList.add(pnlStockListBtns, BorderLayout.SOUTH);

		JButton btnRemove = new JButton("Remove");
		btnRemove.setFont(BTN_FONT);
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onRemoveStockClick();
			}
		});

		pnlStockListBtns.add(btnRemove);

		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(BTN_FONT);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onAddStockClick();
			}
		});
		pnlStockListBtns.add(btnAdd);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setFont(BTN_FONT);

		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onRefreshClicked();
			}
		});
		pnlStockListBtns.add(btnRefresh);
		
				JButton btnSave = new JButton("Save");
				pnlStockListBtns.add(btnSave);
				btnSave.setFont(BTN_FONT);
				btnSave.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						onSaveClicked();
					}
				});

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		pnlStocksList.add(panel, BorderLayout.NORTH);

		JLabel lblStocksInList = new JLabel("Stocks in List:");
		panel.add(lblStocksInList);
		lblStocksInList.setFont(ProtoFrame.BIG_FONT);

		JPanel pnlStockMsg = new JPanel();
		getContentPane().add(pnlStockMsg, BorderLayout.CENTER);
		pnlStockMsg.setLayout(new BorderLayout(0, 0));

		JPanel pnlMsgLbl = new JPanel();
		pnlStockMsg.add(pnlMsgLbl, BorderLayout.NORTH);
		pnlMsgLbl.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel lblMessage = new JLabel("Message:");
		lblMessage.setFont(ProtoFrame.BIG_FONT);
		pnlMsgLbl.add(lblMessage);

		JScrollPane scrollPaneMsg = new JScrollPane();

		pnlStockMsg.add(scrollPaneMsg, BorderLayout.CENTER);

		txtMsg = new JTextArea();
		scrollPaneMsg.setBorder(null);
		txtMsg.setFont(ProtoFrame.BIG_FONT);
		txtMsg.setLineWrap(true);
		txtMsg.setEditable(false);

		scrollPaneMsg.setViewportView(txtMsg);

		JPanel panel_1 = new JPanel();
		pnlStockMsg.add(panel_1, BorderLayout.SOUTH);

		JButton btnDisplayMsg = new JButton("Display");
		panel_1.add(btnDisplayMsg);
		btnDisplayMsg.setFont(BTN_FONT);
		btnDisplayMsg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onDisplayClicked();
			}
		});

		JButton btnOptions = new JButton("Options");
		panel_1.add(btnOptions);
		btnOptions.setFont(BTN_FONT);
		btnOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onOptionsClick();
			}
		});
		scrollPaneMsg.getViewport().getView().setFont(ProtoFrame.BIG_FONT);
		/*
		 * Special commands on start up and close
		 */
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {

			}

			@Override
			public void windowClosed(WindowEvent arg0) {

			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				onWindowClosing();
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {

			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {

			}

			@Override
			public void windowIconified(WindowEvent arg0) {

			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				onWindowOpened();
			}

		});
	}

	/**
	 * Prompt the user with a question asking if they saved TODO implement a
	 * save-checker and only prompt when there is unsaved changes in the list
	 */
	private void onWindowClosing() {

		int close = protocon.attemptClose(this);

		if (close == ProtoController.CLOSE) {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} else {
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}

	}

	/**
	 * TODO implement options later
	 */
	private void onOptionsClick() {
		// JOptionPane.showMessageDialog(this, "Does nothing yet", "Options",
		// JOptionPane.PLAIN_MESSAGE);
		this.protocon.optionsClicked(this);
	}

	private void onWindowOpened() {
		// TODO nothing - start up is all in the ProtoController constructor now
	}

	private void onSaveClicked() {
		protocon.saveAll();
	}

	private void onAddStockClick() {
		protocon.addStock(this);
	}

	private void onRemoveStockClick() {
		protocon.removeSelectedStock();
	}

	/**
	 * Displays whatever type of option the user chooses
	 */
	private void onDisplayClicked() {
		int option = JOptionPane.showOptionDialog(this, "Display Message Options:", "Refresh",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, ProtoController.DISPLAY_OPTIONS,
				ProtoController.DISPLAY_OPTIONS[0]);

		String msg = protocon.refreshMessage(option);
		
		if(msg.equals("")) // TODO replace with a constant
			return;
		
		this.txtMsg.setEditable(true);
		this.txtMsg.setText(msg);
		this.txtMsg.setEditable(false);

	}

	/**
	 * Calls IEX to refresh stock price information
	 */
	private void onRefreshClicked() {
		protocon.refreshStocks();
	}

}
