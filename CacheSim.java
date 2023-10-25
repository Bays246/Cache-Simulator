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
//import java.util.Queue; // Used for fully assoiative to keep track of last used addresses.

public class CacheSim{
	public static void main(String[] args){
		
		// Config
		final int CACHE_BLOCK_SIZE = 8; // Block size of the cache.
		final String ADDRESS_FILE_NAME = "AddressList.txt"; // Name of the file that contains the addresses to simulate reading.
		
		// Set up cache.
		int[] cache = new int[CACHE_BLOCK_SIZE];
		
		// Initilize cache.
		Arrays.fill(cache, -1);
		
		// Arraylist to store addresses in.
		ArrayList<Integer> addressList = new ArrayList<Integer>();
		
		// Get addresses to read in simulation.
		File addressFile = new File(ADDRESS_FILE_NAME);
		if(addressFile.exists()){
			try{
				
				// Open file.
				Scanner addressFileScanner = new Scanner(addressFile);
				
				// Adjust for csv.
				if(ADDRESS_FILE_NAME.endsWith(".csv"))
					addressFileScanner.useDelimiter(",");
				
				// Get contents.
				while(addressFileScanner.hasNextInt()){
					addressList.add( addressFileScanner.nextInt() );
				}
				
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
			System.out.println('"'+ADDRESS_FILE_NAME+'"'+" does not exist, cannot read addresses.");
			System.exit(1);
			
		}
		
		// Boolean array to store hit/misses.
		boolean[] hitMiss = new boolean[addressList.size()];
		
		// Array[row][col] to store the steps[block][step].
		int[][] cacheSimSteps = new int[CACHE_BLOCK_SIZE][addressList.size()];
		
		// Array to store the mod values.
		int[] mods = new int[addressList.size()];
		
		// Simulate reading the addresses and storing them in cache.
		for(int step = 0; step < addressList.size(); step++){
			
			// The address in question.
			int address = addressList.get(step);
			
			// Block number.
			mods[step] = address % CACHE_BLOCK_SIZE;
			
			// Is hit or miss?
			if(address == cache[mods[step]]) hitMiss[step] = true;
			
			// Record the address on miss.
			else cache[mods[step]] = address;
			
			// Record the step.
			for(int block = 0; block < CACHE_BLOCK_SIZE; block++)
				cacheSimSteps[block][step] = cache[block];
			
		}
		
		// Print results.
		System.out.println("Addresses, Mod, Hit, Blocks".replace(' ', ' '));
		System.out.println(addressList.toString().replace(' ', '\t'));
		System.out.println(Arrays.toString(mods).replace(' ', '\t'));
		System.out.println(Arrays.toString(hitMiss).replace(' ', '\t'));
		for(int[] row : cacheSimSteps)
			System.out.println(Arrays.toString(row).replace(' ', '\t'));
		
	}
}