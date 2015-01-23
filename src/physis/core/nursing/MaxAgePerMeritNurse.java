/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: MaxAgePerMeritNurse.java,v 1.6 2000/10/30 17:09:01 sirna Exp $
 * $Revision: 1.6 $
 * $Date: 2000/10/30 17:09:01 $
 */

package physis.core.nursing;

import physis.core.DigitalOrganism;
import physis.core.iterator.DigitalOrganismIterator;
import physis.core.virtualmachine.GeneticCodeTape;
import physis.core.PHYSIS;
import physis.log.Log;

/**
 * A Nurse which favors organisms with high merit and they're not too old.
 */
public class MaxAgePerMeritNurse implements Nurse {
    
    /**
     * A new organism should be placed somewhere in the parent's neighbourhood.
     * The one with the max age/merit will be killed if there is no empty cell.
     * @param newborn The newly created organism.
     * @param oldorgs The neighbourhood of the parent organism.
     */
    public void placeNewBorn(GeneticCodeTape newborn, DigitalOrganismIterator oldorgs, int inherited_length){
        //find an empty cell or one with highest age/merit
        DigitalOrganism chosen = null;
        DigitalOrganism temp = null;
        double agepermerit = 0;
        double chosen_agepermerit = Integer.MIN_VALUE;
        
        try{
	    
	    while (oldorgs.hasNext()) {
		temp = oldorgs.next();
		if (!temp.isAlive()){
		    chosen = temp;
		    break;
		}
		agepermerit = (double) temp.getAge() / (double) temp.getMerit();
		if (agepermerit > chosen_agepermerit){
		    chosen = temp;
		    chosen_agepermerit = agepermerit;
		}
	    }
	    //vivify the chosen organism. (It means killing if the organism was alive before.
	    chosen.vivify(PHYSIS.environment.getNeighbourRandomly(chosen) ,newborn, inherited_length);
        } catch(Exception e){
	    Log.error(e);
        }
        
    }
}








