package itchParser;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class Parsers {
	
	private ParseDS parDS;
	
	// Constructor
	Parsers(ParseDS parDS){
		// Use our ParseDS object
		this.parDS = parDS;	
	}
	
	
	// Given a byte array and length, will return the length
	public String getString(byte[] payload, int len) throws UnsupportedEncodingException{
		// The length needs to be passed as well
		String str = "";
		
		for (int i = 0; i<payload.length; i++){
			str = str + ByteBuffer.wrap(payload).getChar();
		}

		return new String(payload, "UTF8").replaceAll("\\W", "");
	}
	
	// given a byte array will return the Length of payload
	public Object getLen(byte[] payload){
		// Convert to big end
		
		ByteBuffer length = ByteBuffer.wrap(payload);

		return (int) length.getShort(0);
		
	}
	
	// given byte array, will return an int
	public String getInt(byte[] payload){
	
		// int is 4 bytes long
		
	return Integer.toString( ByteBuffer.wrap(payload).getInt() );
	}

	// given byte array, will return an LONG
	public Object getLong(byte[] payload ){
		
		// Return 8 byte long integer value
		
		return Long.toString( ByteBuffer.wrap(payload).getLong() );
	}
	
	// return char at beginning of byte array
	public String getChar(byte[] payload ){
		
		String messageType = new String(payload);

		ByteBuffer payBuf = ByteBuffer.wrap(payload);

		String c = "";
		c = c + messageType.charAt(0);
		
		return c;
	}
	
	// Reverse a byte array-- for big endian
	public  void reverse(byte[] array) {

		
	      if (array == null) {
	          return;
	      }
	      int i = 0;
	      int j = array.length - 1;
	      byte tmp;
	      while (j > i) {
	          tmp = array[j];
	          array[j] = array[i];
	          array[i] = tmp;
	          j--;
	          i++;
	      }
	  }

	// payload in, get the messageType, the fields, then the calc the message  
	public ArrayList<String> messageIn(byte[] payload) throws UnsupportedEncodingException{
		
		// Keep track of where we are in the message
		int messagePointer = 0;
		 
		ArrayList<String> messageArray = new ArrayList<String>();
		ArrayList<Object> fieldsArray = new ArrayList<Object>();
		
		// Get the messageType (getChar only returns A char in the first byte)
		String messageType = getChar(payload).toString();
		
		messageArray.add(messageType);
		
		// increment after look at first char
		messagePointer = messagePointer + 1;
		
		// Get the fields for this message
		fieldsArray = this.parDS.getFields(messageType);
		
		// loop over fields array, parsing messages --start at 1, we already have messagetype
		for (int i = 1; i<fieldsArray.size(); i++){
			
			ArrayList<Object> fieldArray = this.parDS.getFormat( (String) fieldsArray.get(i));
			
			// Split payload, get the array for this field
			byte[] slicedArray  = splitByte(payload, messagePointer, (Integer) fieldArray.get(1) );
			
			// Call appropriate parser on the slicedArray, get the value
			messageArray.add( parse(slicedArray, fieldArray) ); 
			
			// Move the array cursor after looking at this field
			messagePointer = messagePointer + ((Integer) fieldArray.get(1));
		}

		//System.out.println("MESSAGE ARR: " + messageArray.toString());
		return messageArray;
		
	}
	
	// Given input byteArray, start & len-- output new array
	public byte[] splitByte(byte[] byteArray, int start, int len){
		
		byte[] slicedArray = new byte[len];
		
		// Loop through old array, create new
		for (int i = 0; i< len; i++ ){
			slicedArray[i] = byteArray[start+i];
		}
		
		return slicedArray;
	}


	// With input byteArray, len & parse type, call approp parser
	public String parse(byte[] arr, ArrayList<Object> fieldArray) throws UnsupportedEncodingException{
		String value = null;
		
		
		switch( (Integer) fieldArray.get(0) ){
		
		case 1:	value = (String) getChar(arr);
				break;
		
		case 2: value = (String) getInt(arr);		
				break;
			
		case 3:	value = (String) getString(arr, (Integer) fieldArray.get(1));
				break;
			
		case 4: value = (String) getLong(arr);
				break;

		}
		
		return value;
	}
	
}
