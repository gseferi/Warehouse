package serobot.pcinterface;

/**
 * Test OS
 * @author KWONG HEI TSANG
 *
 */
public class TestOS {

	public static void main(String[] args){
		System.out.println(System.getProperty("os.name"));
		System.out.println(System.getProperty("os.name").startsWith("Windows"));
	}
}
