package unusedTrash;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**
 * Reads and writes files for the stock program.
 * 
 * @author William
 *
 */
public class FileInterpreter {

	private File target;

	public FileInterpreter(boolean doWork) {
		if(doWork == false) {
			return;
		}
		
		/*
		 * create a file at given path if doesnt exist
		 */
		target = new File(Controller.FILEPATH);

		if (!target.exists()) {

			try {

				target.createNewFile(); // TODO difference between mkdir, mkdirs, and createNewFile

			} catch (IOException e) {

				e.printStackTrace();

			}
		}

	}
	/**
	 * Writes the msg to the target file.
	 * 
	 * @param msg
	 */
	public void writeStockMessage(String msg) {
		
		// for writing the file
		BufferedWriter bwriter = null;
		FileWriter fwrite;
		
		// read the file later to double check if we wrote the msg properly
		BufferedReader breader = null;
		FileReader freader = null;
		
		/*
		 * writing the msg
		 */
		try {
			System.out.println("writing the msg: " + msg);
			fwrite = new FileWriter(this.target);
			bwriter = new BufferedWriter(fwrite);
			bwriter.write(msg);

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (bwriter != null) {

				try {

					bwriter.close();

				} catch (IOException e) {

					e.printStackTrace();

				}

			}
		}
		
		/*
		 * reading the msg and double checking it wrote properly
		 */
		try {
			freader = new FileReader(this.target);
			breader = new BufferedReader(freader);
			String ln = breader.readLine();
			System.out.println("ln is " + ln);
			if (!ln.equals(msg)) {
				System.err.println(
						"The stock message did not write to the file perfectly the same -- \n FileInterpreter.writeStockMessage(String : msg)");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (breader != null) {
				try {
					breader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
