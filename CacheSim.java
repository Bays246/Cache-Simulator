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
		
		// Generate console scanner.
		Scanner consoleInput = new Scanner(System.in);
		consoleInput.useDelimiter("\\s*\n\\s*");
		
		// Variables
		int cacheSize = 0, cacheBlockSize = 0, cacheBlocks = 0, addressLengthInBits = 0;
		String addressFileName = "";
		boolean verboseOutput = false;
		File addressFile = new File("");
		
		// User input config.
		try{
			
			// Get cache size, total addresses the cache can fit.
			System.out.print("Cache size: ");
			cacheSize = consoleInput.nextInt();
			
			// Get cache block size, aka number of cache levels.
			System.out.print("Cache block size: ");
			cacheBlockSize = consoleInput.nextInt();
			
			// Validate cache block size.
			while(cacheSize % cacheBlockSize != 0 || cacheBlockSize > cacheSize){
				if(cacheBlockSize > cacheSize){
					System.out.println("Cache block size must be equal to or less than cache size.");
					System.out.print("Cache block size: ");
					cacheBlockSize = consoleInput.nextInt();
				}else{
					System.out.println("Cache block size must evenly divide cache size.");
					System.out.print("Cache block size: ");
					cacheBlockSize = consoleInput.nextInt();
				}
			}
			
			// Get address file name, to read addresses from.
			System.out.print("Address file name: ");
			addressFile = new File(consoleInput.next());
			
			
			// Validate address file name.
			while(!addressFile.exists()){
				System.out.println('"'+addressFile.toString()+'"'+" does not exist.");
				System.out.print("Address file name: ");
				addressFile = new File(consoleInput.next());
			}
			
			// Get verbose output preference, aka show every step.
			System.out.print("Verbose output (show every step) (y/n): ");
			if(consoleInput.next().equalsIgnoreCase("y")) verboseOutput = true; // Else it was already set to false earlier.
			
			// Nice looking newline.
			System.out.println();
			
		}catch(Exception e){
			
			// Something went wrong.
			System.out.println("Something went wrong while getting user input.");
			System.out.println(e.toString());
			System.exit(1);
			
		}
		
		// Arraylist to store addresses in.
		ArrayList<Integer> addressList = new ArrayList<Integer>();
		
		// Get addresses from file.
		try{
			
			// Open file.
			Scanner addressFileScanner = new Scanner(addressFile);
			
			// Default radix (read in base 2).
			addressFileScanner.useRadix(2);
			
			// Get first value and get how many bits in an address.
			if(addressFileScanner.hasNext()){
				String tmpString = addressFileScanner.next();
				addressLengthInBits = tmpString.length();
				addressList.add(Integer.parseInt(tmpString, 2));
			}
			
			// Get contents.
			while(addressFileScanner.hasNext()) addressList.add( addressFileScanner.nextInt() );
			
			// Close file.
			addressFileScanner.close();
			
		}catch(Exception e){
			
			// Something went wrong while reading addresses.
			System.out.println("Something went wrong while reading addresses.");
			System.out.println(e.toString());
			System.exit(1);
			
		}
		
		// ---------- Cache Simulation -----------
		
		// Create cache[rows][cols] aka cache[Block][Level].
		cacheBlocks = cacheSize / cacheBlockSize;
		int[][] cache = new int[cacheBlocks][cacheBlockSize];
		
		// Initilize cache, -1 means empty cell.
		for(int[] row : cache) Arrays.fill(row, -1);
		
		// Create hit/miss array, -1 means miss, 0 means found in first level and so on.
		int[] hitMiss = new int[addressList.size()];
		
		// Initilize hit/miss array.
		Arrays.fill(hitMiss, -1);
		
		// DEBUG: Need this in case of verbose printing.
		consoleInput.nextLine();
		
		// Loop for every address in address list.
		for(int addressIndex = 0; addressIndex < addressList.size(); addressIndex++){
			
			// Figure out the current address.
			int currentAddress = addressList.get(addressIndex);
			
			/* TODO: Figure this out at some point.
			// Search for the address in the cache.
			for(int cacheLevel = 0; cacheLevel < cacheBlockSize; cacheLevel++){
				
				// Mod the address to figure out what block it would be in.
				int blockIndex = currentAddress % cacheBlocks;
				
				// Adjust the block based on what level of the cache is being searched.
				blockIndex -= cacheLevel;
				
				// Account for underflow.
				if(blockIndex < 0){
					blockIndex += Math.pow(2, addressLengthInBits); // Add the max possible address value.
					blockIndex %= cacheBlocks; // Re-mod the address.
				}
				
				// Search the chosen index of the cache.
				if(cache[blockIndex][cacheLevel] == currentAddress) hitMiss[addressIndex] = cacheLevel;
				
			}
			*/
			
			// Search for address in cache.
			for(int[] block : cache){
				for(int i = 0; i < block.length; i++){
					if(block[i] == currentAddress) hitMiss[addressIndex] = i;
				}
			}
			
			// Load addresses into the cache on a miss.
			if(hitMiss[addressIndex] == -1){
				for(int cacheLevel = 0; cacheLevel < cacheBlockSize; cacheLevel++){
					
					// Mod the address to figure out what block it would be in.
					int blockIndex = currentAddress % cacheBlocks;
					
					// Prepare the address to save in the cache.
					int addressToSave = currentAddress + cacheLevel;
					
					// Account for overflow.
					addressToSave %= Math.pow(2, addressLengthInBits);
					
					// Save to chosen cache cell.
					cache[blockIndex][cacheLevel] = addressToSave;
					
				}
			}
			
			// Verbose printing.
			if(verboseOutput){
				
				// Print this step.
				System.out.println("Current address: "+currentAddress+" - "+((hitMiss[addressIndex] == -1)? "miss" : "hit on level " + hitMiss[addressIndex]));
				System.out.println("Cache after processing:");
				for(int[] row : cache) System.out.println( Arrays.toString( row ) );
				
				// Wait for acknowledgement to continue.
				consoleInput.nextLine();
				
			}
		}
		
		// Tally results.
		int totalHits = 0;
		int[] hitsOnLayer = new int[cacheBlockSize];
		for(int result : hitMiss){
			if(result != -1){
				totalHits++;
				hitsOnLayer[result]++;
			}
		}
		
		// Print results.
		System.out.println("Total Hits: "+totalHits+" out of "+addressList.size()+" addresses.");
		
		System.out.println("Hits by level");
		System.out.print("Level:[0");
		for(int i = 1; i < cacheBlockSize; i++) System.out.print(", "+i);
		System.out.println(']');
		System.out.println("Hits: "+Arrays.toString(hitsOnLayer));
		
		System.out.println("Bits per address: "+addressLengthInBits);
		
		// Close the console scanner.
		consoleInput.close();
	}
}

/*
for(int i : addressList){
	tmp = ("0000000"+Integer.toBinaryString(i)); // Change number of zeros.
	System.out.println( tmp.substring(tmp.length()-7) ); // Length - # zeros.
}
*/