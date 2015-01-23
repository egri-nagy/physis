/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: RandomnessFactory.java,v 1.1 2001/07/06 08:35:24 sirna Exp $
 * $Revision: 1.1 $
 * $Date: 2001/07/06 08:35:24 $
 */
package physis.core.random;

import physis.core.Configuration;
import physis.log.Log;

public class RandomnessFactory{

    private static String RANDOM_SEED = "random_seed";
    private static String RANDOM_GENERATOR = "random_number_generator";
    
    public static Randomness createRandomness(){
	String seed = Configuration.getProperty(RANDOM_SEED);
	long l = 0;
	if ((seed == null) || ("".equals(seed))){
	    l = System.currentTimeMillis();
	    Log.status("RANDOM SEED:  timestamp - " + l);
	}
	else{
	    Log.status("RANDOM SEED: " + seed);
	    l = Long.parseLong(seed);
	}
	String type = Configuration.getProperty(RANDOM_GENERATOR);
        if ("MersenneTwister".equals(type)){
	    Log.status("Random Number Genarator: Mersenne-Twister");
	    return new MersenneTwister(l);
	}
	else if ("JavaUtilRandom".equals(type)){
	    Log.status("Random Number Genarator: JavaUtilRandom");
	    return new JavaUtilRandom(l);
	}
	else{
	    Log.error("No valid random number generatro given!");
	    return null;
	}
    }
}
