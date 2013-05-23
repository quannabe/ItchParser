package itchParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Parse will take an ITCH 4.1 binary file as input, read that file as byte arrays
 * Then, if the user wants, parse the message to human-readable format via the Parsers and print
 * Finally, it will return the message as a byte array
 * 
 * @author William Sell
 * 
 * quannabe@gmail.com
 *
 */


	public class Parse {
		
		private String filename;
		
		// This is the yaml file where the
		private String yamlFile = "itch41.yaml";
		
		private boolean parseNPrint;
		
		private byte[] lenBytes = new byte[2];
		
		private InputStream input;

		Parsers parsers;
		ParseDS parseDS;

		// Constructor with the ITCH4.1 binary file location as arg
		public Parse(String filename, boolean parseNPrint) {
		
		// Check arg for printing parsing the message 
		this.parseNPrint = parseNPrint;
		
		// Via YAML, create the Data Structures 
		try {
			parseDS = new ParseDS(yamlFile);
		} catch (FileNotFoundException e) {
			System.out.println("File not found...Check Filename");
			e.printStackTrace();
		}
		
		// Create the parsers with the YAML data structures
		parsers = new Parsers(parseDS);
		this.filename = filename;
			

		// Open the data file
		try {
			input = new FileInputStream(new File(filename));
		} catch (FileNotFoundException e) {
			System.out.println("File not found...Check Filename");
			e.printStackTrace();
		}
		
	}
		
		// Read the message from binary file
		public byte[] parse(int count) throws IOException, InterruptedException{
			
			// Get the message length
			input.read(lenBytes);
			
			//  Add EOF conditions
			int payLength =  (ByteBuffer.wrap(lenBytes)).getShort(0);
			
			
			if (0 == -1){
				return null;
			}
			
			if (count% 1000000 == 0){
				System.out.println((ByteBuffer.wrap(lenBytes)).getShort());
				System.out.println("available: " + input.available());

			}

			// Get the payload
			byte[] payBytes = new byte[payLength];
			input.read(payBytes);

			// Check if we are parsing and printing
			if (parseNPrint){
				
				ArrayList<String> messageArr = null;
				
				// Send payBytes byte[] to Parsers for processing
				// An arraylist will be returned with the message
				messageArr = (parsers.messageIn(payBytes));
				
				System.out.println( messageArr);
			}
			return payBytes;
		}
		
		// main test harness
		public static void main(String args[]) throws IOException, InterruptedException{
		
			
			// ITCH file location
			// /Users/William/Desktop/storm/python/cdtitch41/data/09142012.itch41
			String filename = "/Users/William/Desktop/storm/python/cdtitch41/data/09142012.itch41";
			
			
			// ITCH file location
			//String filename = "/location/of/your/itch/binary/file.itch41";
			
			// Set this to 'false' to supress output
			Boolean PP = false;
			
			Parse parse = new Parse(filename, PP);
			
			int count = 0;
			
			final long startTime = System.currentTimeMillis();
			
			while (parse.parse(count) != null){
				

				if (count% 1000000 == 0){
					System.out.println("count: " + count);
				}
				count ++;
			}
			final long endTime = System.currentTimeMillis();

			System.out.println("Total execution time: " + (endTime - startTime)* 0.001 );
			System.out.println("Messages/second: " + count/((endTime - startTime)* 0.001) );

		}
}
