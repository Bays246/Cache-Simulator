/*
  CS 231 Project Option 3
  Jaden Lagos, Adam Cox, Hunter Bays.
  
  ADDRESS_FILE_NAME expects either a .csv or .txt file.
  .csv expects the "," delimiter.
  .txt expects any whitespace delimiter.
*/

import java.io.File;  // Necessary to identify the file to read from.
import java.util.Scanner; // Necessary to read files.
import java.util.ArrayList; // Needed to have a mutable array to store addresses in.
import java.util.Arrays; // Makes working with arrays easier.

public class CacheSim{
	public static void main(String[] args){
		
		// Variables
		int cacheSize = 0, cacheBlockSize = 0, cacheLines = 0;
		String addressFileName = "";
		
		// User input config.
		try{
			
			// Generate console scanner.
			Scanner consoleInput = new Scanner(System.in);
			consoleInput.useDelimiter("\\s*\n\\s*");
			
			// Cache Size.
			System.out.print("Cache Size: ");
			cacheSize = consoleInput.nextInt();
			
			// Cache Block Size.
			System.out.print("Cache Block Size: ");
			cacheBlockSize = consoleInput.nextInt();
			
			// Address file name (to read addresses from).
			System.out.print("Address file name: ");
			addressFileName = consoleInput.next();
			
			// Close the console scanner.
			consoleInput.close();
			
		}catch(Exception e){
			
			// Something went wrong.
			System.out.println("Something went wrong while getting user input.");
			System.out.println(e.toString());
			System.exit(1);
			
		}
		
		// Set up cache[rows][cols].
		cacheLines = cacheSize / cacheBlockSize;
		int[][] cache = new int[cacheLines][cacheBlockSize];
		
		// Initilize cache.
		for(int[] row : cache)
			Arrays.fill(row, -1);
		
		// Arraylist to store addresses in.
		ArrayList<Integer> addressList = new ArrayList<Integer>();
		
		// Get addresses to read in simulation.
		File addressFile = new File(addressFileName);
		if(addressFile.exists()){
			try{
				
				// Open file.
				Scanner addressFileScanner = new Scanner(addressFile);
				
				// Default radix (read in base 2).
				addressFileScanner.useRadix(2);
				
				// Get contents.
				while(addressFileScanner.hasNext())
					addressList.add( addressFileScanner.nextInt() );
				
				// Close file.
				addressFileScanner.close();
				
			}catch(Exception e){
				
				// Something went wrong.
				System.out.println("Something went wrong while reading addresses.");
				System.out.println(e.toString());
				System.exit(1);
				
			}
		}else{
			
			// There was no file to read addresses from.
			System.out.println('"'+addressFileName+'"'+" does not exist, cannot read addresses.");
			System.exit(1);
			
		}
		
		// Print results.
		System.out.println("Integer: ");
		for(int i : addressList)
			System.out.print(""+i+" ");
		System.out.println('\n');
		
		System.out.println("Binary: ");
		String tmp;
		for(int i : addressList){
			tmp = ("0000000"+Integer.toBinaryString(i)); // Change number of zeros.
			System.out.println( tmp.substring(tmp.length()-7) ); // Length - # zeros.
		}
		System.out.println('\n');
	}
}

/*
for(int i : addressList){
	tmp = ("0000000"+Integer.toBinaryString(i)); // Change number of zeros.
	System.out.println( tmp.substring(tmp.length()-7) ); // Length - # zeros.
}
*/