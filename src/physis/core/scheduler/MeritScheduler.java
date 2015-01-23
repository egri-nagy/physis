/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: MeritScheduler.java,v 1.7 2000/09/01 09:09:48 sirna Exp $
 * $Revision: 1.7 $
 * $Date: 2000/09/01 09:09:48 $
 */
package physis.core.scheduler;

import java.util.*;
import physis.core.DigitalOrganism;
import physis.core.iterator.DigitalOrganismIterator;
import physis.log.Log;

/**
 * Another dummy scheduler. The organism receives the same number of sticks as its merit.
 * Newborns with zero merit get the average.
 */
public class MeritScheduler implements Scheduler {
    
    DigitalOrganismIterator orgs;
    
    public void scheduleUpdate(int average){
        try{
	    orgs.reset();
	    
	    DigitalOrganism digorg = null;
	    while (orgs.hasNext()){
		digorg = orgs.next();
		if (digorg.isAlive()) {
		    digorg.update(digorg.getMerit());
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
