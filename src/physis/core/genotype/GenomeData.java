/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Revision: 1.5 $
 * $Date: 2001/04/18 06:43:20 $
 */

package physis.core.genotype;

/**
 * Some useful information about a genome.
 */
public class GenomeData {
    /** The name of the genome: <length>_<sequence code>  e.g.: 72_bAe */
    private String name;
    
    
    private int number_of_births = 1;  //when creating it's already one
    private int number_of_spawns = 0;  //when appearing it's of course 0
    /** The update number when the genome first occurs in the system. */
    private long timestamp;
    /** The update number when last time something happened to this genotype. */
    private long last_touched;
    /** The reference to the parent genome. */
    private Genome parent;
    
    public GenomeData(long timestamp_){
	timestamp = timestamp_;
    }
    
    public GenomeData(long timestamp_, Genome parent_){
	timestamp = timestamp_;
	parent = parent_;
    }
    
    public void increaseNumberOfSpawns() { number_of_spawns++; }
    public void increaseNumberOfBirths() { number_of_births++; }
    
    public int getNumberOfSpawns() { return number_of_spawns; }
    public int getNumberOfBirths() { return number_of_births; }
    
    public void setName(String new_name){
	name = new_name;
    }
    
    public String getName(){return name;}
    
    public void setLastTouch(long current_update){
	last_touched = current_update;
    }
    
    /**
     * When the genotype is inactive for a given period, than it is suppposed to be extinct.
     * This is for viable genomes since the sterile are removed because of the expiration time.
     */
    public boolean isFossil(int fossil_time, long current){
	return fossil_time < (current - last_touched);
    }
    
    /**
     * This is used for deciding whether the genome should be removed or not. There's a trial
     * period for new genomes in which the genome should create more children than a specified
     * threshold value.
     */
    public boolean isExpired(int expiration_time, long current){
	return expiration_time < (current - timestamp);
    }
    
    public String toString(){
	return "Name: " + name+"#" + "\nBirths: " + number_of_births + "\nSpawns: " + number_of_spawns
	    + "\nCreated:" + timestamp +
	    (last_touched != timestamp ? "\nLast life sign:" + last_touched + "\nLifetime: " + (last_touched - timestamp) + "\n" : "\nOnly appeared once.\n");
	//+ "\nParent: " + parent + "\n";
    }
}
