/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Revision: 1.10 $
 * $Date: 2001/05/09 12:30:23 $
 */

package physis.core.genotype;

import java.util.*;
import java.io.*;
import physis.core.Configuration;
import physis.core.PHYSIS;
import physis.core.virtualmachine.InstructionSet;
import physis.log.Log;

/**
 * Stores (dynamically and persistently) the genetic information of the whole population.
 */
public class GeneBank {

    private static final String BIRTH_THRESHOLD = "birth_threshold";
    private static final String SPAWN_THRESHOLD = "spawn_threshold";
    private static final String OUTPUT_FILE = "gene_bank_output_file";
    private static final String EXPIRATION_TIME = "expiration_time";
    private static final String FOSSIL_TIME = "fossil_time";

    /** Keys are Genomes, values are GenomeDatas. */
    private static Hashtable bank = new Hashtable();

    private static PHYSIS physis = PHYSIS.getInstance();

    public static void register(Genome parent, Genome child){
	long current_update = physis.getUpdateCount();
	//registering parent
	GenomeData gd = null;
	if (bank.containsKey(parent)){
	    gd = (GenomeData) bank.get(parent);
	    gd.increaseNumberOfSpawns();
	    gd.setLastTouch(current_update);
	}
	else{
	    gd = new GenomeData(current_update);
	    gd.setName(parent.size() + "_" + getNextSequence());
	    gd.setLastTouch(current_update);
	    bank.put(parent, gd);
	}

	//registering child
	if (bank.containsKey(child)){
	    gd = (GenomeData) bank.get(child);
	    gd.increaseNumberOfBirths();
	    gd.setLastTouch(current_update);
	}
	else{
	    gd = new GenomeData(current_update, parent);
	    gd.setName(child.size() + "_" + getNextSequence());
	    gd.setLastTouch(current_update);
	    bank.put(child, gd);
	}

	if (bank.size() >= Configuration.getGeneBankMaxSize()){
	    writeFossils();
	}
    }

    public static String getGenomeName(Genome g){
	//self-modifiers are not in the genebank..
	if (!bank.containsKey(g)) return null;
	GenomeData gd = (GenomeData) bank.get(g);
	return gd.getName();
    }

    public static void storePersistently(){
    }

    public static void printAsText(){
	try{
	    PrintWriter pw = new PrintWriter(
		new FileWriter(Configuration.getPhysisHome()
				   + Configuration.getProperty(OUTPUT_FILE)
				   + "_"
				   + InstructionSet.getInstance().getName()
				   + "_"
				   + Log.getTimeStamp()
				   + ".dat"));

	    pw.println("Size: " + bank.size());
	    Enumeration e = bank.keys();
	    while (e.hasMoreElements()){
		Object o = e.nextElement();
		pw.print("\n" + bank.get(o) + "\n" + o + "\n");
	    }
	    pw.close();
	}catch (Exception e) { e.printStackTrace();}
    }

    public static void writeFossils(){
	try{
	    long current_update = physis.getUpdateCount();
	    Log.status("Genebank full, automatic processing started at time:" + current_update);
	    Log.status("Genebank size before cleaning up: " + bank.size());
	    int spawn_threshold = Integer.parseInt(Configuration.getProperty(SPAWN_THRESHOLD));
	    int expiration_time = Integer.parseInt(Configuration.getProperty(EXPIRATION_TIME));
	    int fossil_time = Integer.parseInt(Configuration.getProperty(FOSSIL_TIME));
	    Log.status("Expiration time: " + expiration_time + " Fossil time:" + fossil_time + " Spawn threshold: " + spawn_threshold);
	    FileWriter pw = new FileWriter(Configuration.getPhysisHome()
					       + Configuration.getProperty(OUTPUT_FILE)
					       + "_"
					       + InstructionSet.getInstance().getName()
					       + current_update + ".dat",true);

	    int number_of_spawns;
	    int removed = 0;
	    int written = 0;
	    Enumeration keys = bank.keys();
	    while (keys.hasMoreElements()){
		Object key = keys.nextElement();
		GenomeData gd = (GenomeData) bank.get(key);
		number_of_spawns = gd.getNumberOfSpawns();
		//when the spawn_threshold is et to zero
		if ( (gd.isExpired(expiration_time, current_update)) && (number_of_spawns < spawn_threshold) ){
		    bank.remove(key);
		    removed++;
		}
		else if (gd.isFossil(fossil_time, current_update)){
		    pw.write(bank.get(key) + "\n" + key);
		    bank.remove(key);
		    written++;
		}
	    }
	    pw.close();
	    Log.status("Removed: " + removed + " Written:" + written+ "\n");
	    Log.status("Genebank size after cleaning up: " + bank.size());
	    System.gc();
	    Log.printMemoryInfo();
	    Log.status("\n");
	}catch (IOException ioe){ Log.error(ioe); }
    }

    private static long prev_seq;
    private static String getNextSequence(){
	return "" + prev_seq++;
    }

    public static boolean isEmpty(){return bank.isEmpty(); }

}
