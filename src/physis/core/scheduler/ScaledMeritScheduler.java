/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: ScaledMeritScheduler.java,v 1.2 2001/06/13 07:34:16 sirna Exp $
 * $Revision: 1.2 $
 * $Date: 2001/06/13 07:34:16 $
 */
package physis.core.scheduler;

import physis.core.*;
import physis.core.iterator.DigitalOrganismIterator;
import physis.log.Log;

import java.util.*;

/**
 * A somewhat more sophisticated scheduler. It scales the merit so the average means average approximately.
 * The number of actually given sticks don't grow limitless as with the MeritScheduler.
 */
public class ScaledMeritScheduler implements Scheduler {
    
    DigitalOrganismIterator orgs;
    
    public void scheduleUpdate(int average){
       // try{
            //some correction is needed in order not to get infinity ratio in the beginning
            double population_average = PHYSIS.getInstance().getPopulation().getAverageMerit();
	    double ratio = (double) average /  ((population_average < 1.0) ? 1.0 : population_average);
	    
	    orgs.reset();
	    
	    DigitalOrganism digorg = null;
	    while (orgs.hasNext()){
		digorg = orgs.next();
		if (digorg.isAlive()) {
			digorg.update((int) Math.ceil(digorg.getMerit() * ratio));
		}
	    }
        /*} catch (Exception e){
	    e.printStackTrace();
	    Log.error(e.getMessage() + " in ScaledMeritScheduler");
	 }*/
    }
    
    public void fillWithOrganisms(DigitalOrganismIterator orgs_){
        orgs = orgs_;
    }
    
}
