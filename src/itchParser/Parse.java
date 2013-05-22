package itchParser;

import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.nodes.Tag;


	public class Parse {
		
		// ITCH file location
		// /Users/William/Desktop/storm/python/cdtitch41/data/09142012.itch41
		
		private String filename;
		private String yamlFile = "itch41.yaml";
		
		private InputStream input;

		Yaml yaml;
		Parsers parsers;
		ParseDS parseDS;

		// Constructor
		public Parse(String filename) {
		
		// initialize the parsers, build out data structure
		yaml = new Yaml();
		try {
			parseDS = new ParseDS(yaml, yamlFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		parsers = new Parsers(parseDS);
		this.filename = filename;
			

		// Open the data file
		try {
			input = new FileInputStream(new File(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		

	
		// Generate stream of tuples
		//public ArrayList<String> parse() throws IOException, InterruptedException{
		public byte[] parse() throws IOException, InterruptedException{
			
			ArrayList<String> messageArr = null;

			// Get the message length
			byte[] lenBytes = new byte[2];
			input.read(lenBytes);
			
			//  Add EOF conditions
			
			int payLength =  (Integer) parsers.getLen(lenBytes);

			//System.out.println("Message length: " + parsers.getLen(lenBytes));

			// Get the payload
			byte[] payBytes = new byte[payLength];
			input.read(payBytes);
			
			
			
			// TESTING ONLY
			/*
			ByteBuffer payBuf = ByteBuffer.wrap(payBytes);
			
			for (int i= 0; i<payLength; i++){
				System.out.println(payBuf.get());
			}
			*/
			
			
			// Send payBytes to Parsers for processing
			// An arraylist will be returned which will be outputted tuples
			
			messageArr = (parsers.messageIn(payBytes));
			
			// Comment below to supress printing message at each iteration
			System.out.println( messageArr) ;
			
			
			return payBytes;

		}
		
		// main test harness
		public static void main(String args[]) throws IOException, InterruptedException{
		
			Parse parse = new Parse("/Users/William/Desktop/storm/python/cdtitch41/data/09142012.itch41");
			
			
			int numMessages = 50000000;
			
			final long startTime = System.currentTimeMillis();
			
			for (int i = 0; i<numMessages; i++){
				parse.parse();

				//System.out.println("count: " + i);
			}
			final long endTime = System.currentTimeMillis();

			System.out.println("Total execution time: " + (endTime - startTime)* 0.001 );
			System.out.println("Messages/second: " + numMessages/((endTime - startTime)* 0.001) );

		}
}
