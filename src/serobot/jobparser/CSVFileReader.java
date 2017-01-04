package serobot.jobparser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSVFileReader {
	private String filename;

	public CSVFileReader(String filename) {
		this.filename = filename;
	}

	public String read() {
		BufferedReader br;
		String everything = null;
		try {
			br = new BufferedReader(new FileReader(filename));

			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			everything = sb.toString();
			br.close();

		} catch (FileNotFoundException e) {
			System.err.println("Exception: File not found - " + filename);
			return null;
		} catch (IOException e) {
			System.err.println("Exception: IOException");
			return null;
		}
		return everything;
	}
}
