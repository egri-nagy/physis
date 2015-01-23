/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: BonusMultipliedConstantScheduler.java,v 1.1 2003/03/05 12:05:23 sirna Exp $
 * $Revision: 1.1 $
 * $Date: 2003/03/05 12:05:23 $
 */
package physis.core.scheduler;

import java.util.*;
import physis.core.DigitalOrganism;
import physis.core.iterator.DigitalOrganismIterator;
import physis.log.Log;

/**
 * Size neutral task-favouring scheduler. Average is multiplied by bonusmultiplier.
 * Newborns with bonus 1.0  get the average.
 */
public class BonusMultipliedConstantScheduler implements Scheduler {
    
    DigitalOrganismIterator orgs;
    
    public void scheduleUpdate(int average){
        try{
	    orgs.reset();
	    
	    DigitalOrganism digorg = null;
	    while (orgs.hasNext()){
		digorg = orgs.next();
		if (digorg.isAlive()) {
		    digorg.update((int)Math.ceil(digorg.getBonusMultiplier() * average));
		}
	    }
        } catch (Exception e){
	    Log.error(e.getMessage() + " in MeritScheduler");
        }
    }
    
    public void fillWithOrganisms(DigitalOrganismIterator orgs_){
        orgs = orgs_;
    }
    
}
