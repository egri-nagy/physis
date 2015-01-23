/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: OldestNurse.java,v 1.6 2000/10/06 15:31:16 sirna Exp $
 * $Revision: 1.6 $
 * $Date: 2000/10/06 15:31:16 $
 */

package physis.core.nursing;

import physis.core.DigitalOrganism;
import physis.core.iterator.DigitalOrganismIterator;
import physis.core.virtualmachine.GeneticCodeTape;
import physis.core.PHYSIS;
import physis.log.Log;

/**
 * A Nurse which kills the oldest organism in the neighbourhood if there's no empty cell.
 */
public class OldestNurse implements Nurse {
    /**
     * A new organism should be placed somewhere in the parent's neighbourhood. The oldest will be killed.
     * @param newborn The newly created organism.
     * @param oldorgs The neighbourhood of the parent organism.
     */
    public void placeNewBorn(GeneticCodeTape newborn, DigitalOrganismIterator oldorgs, int inherited_length){
        //find the oldest or an empty cell
        DigitalOrganism chosen = null;
        DigitalOrganism temp = null;
        
        try{
            
            chosen = oldorgs.next();
            //do the iteration only if the first is alive
            if (chosen.isAlive()){
                while (oldorgs.hasNext()) {
                    temp = oldorgs.next();
                    if (!temp.isAlive()){
                        chosen = temp;
                        break;
                    }
                    if (temp.getAge() > chosen.getAge()) chosen = temp;
                }
            }
            chosen.vivify(PHYSIS.environment.getNeighbourRandomly(chosen) ,newborn, inherited_length);
        } catch(Exception e){
            Log.error(e);
        }
        
    }
}


