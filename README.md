#ItchParser

##NASDAQ ITCH is...

#### "...the standard NASDAQ data feed for serious traders — displays the full order book depth for NASDAQ market participants. TotalView also disseminates the Net Order Imbalance Indicator (NOII) for the NASDAQ Opening and Closing Crosses and NASDAQ IPO/Halt Cross."####

More here:
https://www.nasdaqtrader.com/Trader.aspx?id=totalview


####NASDAQ ITCH 4.1 specification: 
http://www.nasdaqtrader.com/content/technicalsupport/specifications/dataproducts/nqtv-itch-v4_1.pdf


##What does ItchParser do?
Given an ITCH 4.1 binary file as input, it will parse the messages per the above specification, and return them as byte arrays, and/or translate them to human-readable format & print to stdout.

####Speed?
On my 2.3Ghz Intel running Mac OSX 10.7.5 w/ 4GB RAM, ItchParser read & outputted at an average speed of: **~550k messages/second**

NOTE: Parsing the byte array & printing to stdout is considerable slower.


##YAML
The itch41.yaml file is used by the SnakeYaml parser to generate the data structures to translate our message byte array to a human readable format.


##Example:

####Download an Example NASDAQ-ITCH4.1 file 
Download: ftp://emi.nasdaq.com/ITCH/09142012.itch41.gz **(Warning: ~3GB)**


####Add the snakeYAML jars to your file-- I used 1.1:
Download: https://code.google.com/p/snakeyaml/downloads/detail?name=SnakeYAML-all-1.11.zip&can=2&q=

####Run an example:

Create new Parse Object:
`Parse parse = new Parse(filename, ParseAndPrint);`
      
To get the next message:
`myByteArray = parse.parse();`
      
'filename' is the location if the Binary file, and 'ParseAndPrint' is a boolean value: true to parse the message to human readable format & print to stdout, false to turn off. Either way, it will return the message in a byte array.

####Also, see the example in the main method of the Parse.java class to check out a working implementation.


