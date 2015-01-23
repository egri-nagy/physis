package physis.core.random;

import java.util.Random;

/**
 * RandomnessTest class.
 * This code does not really scientifically analyze or validate the
 * random number generators ran here. This will come in at a later date.
 * Specifically, I want to implement the Die Hard tests.
 */
public class RandomnessTest {
    public static final int DIGITS = 10;
    
    public static void main (String args[]) {
	
	long seed = System.currentTimeMillis();
	if(args.length > 0) {
	    seed = Long.parseLong(args[0]);
	} else {
	    System.out.println("Using current time as seed; seed=" + seed);
	}
	
	Randomness myRandom = new MersenneTwister(seed);
	
	// Allocate and initialize the array
	int [] ndigits = new int[DIGITS];
	for (int i = 0; i < DIGITS; i++) {
	    ndigits[i] = 0;
	}
	
	int n = 0;
	// Test the random number generator a whole lot
	for (long i=0; i < 100000; i++) {
	    // generate a new random number between 0 and 9
	    n = myRandom.nextInt(DIGITS);
	    
	    //count the digits in the random number
	    ndigits[n]++;
	}
	
	// Print the results
	for (int i = 0; i < DIGITS; i++) {
	    System.out.println(i+": " + ndigits[i]);
	}
    }
    
}
