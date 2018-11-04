package ProtoPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Interfaces between the program and the stock ticker sign.
 * 
 * @author William
 *
 */
public class SignMediator {

	public static final String IP_ADDRESS = "10.23.122.11"; // TODO InetAddress?
	public static final int PORT = 40001; // TODO

	public SignMediator() {

	}
/*
 * telnet hit the port
 * if server is on other side tcp, might respond with a prompt
 * if http, get and give a url
 * clear text protocol?
 * what is protocol? what is supported at port 4001
 * 
 */
	public void connectToSign() {
		
		Socket socket = null;
		BufferedReader buffr = null;

		try {
			socket = new Socket(IP_ADDRESS, PORT);

			// OutputStream ostream = socket.getOutputStream();

			InputStream istream = socket.getInputStream();

			buffr = new BufferedReader(new InputStreamReader(istream));
			String cur = buffr.readLine();

			while (cur != null) {
				System.out.println(cur);
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
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

			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Sends the message to the sign
	 * 
	 * @param msg
	 */
	public void transmit(String msg) {

	}

}
