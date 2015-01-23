/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: ConstantScheduler.java,v 1.6 2000/07/28 18:06:55 sirna Exp $
 * $Revision: 1.6 $
 * $Date: 2000/07/28 18:06:55 $
 */
package physis.core.scheduler;

import java.util.*;
import physis.core.DigitalOrganism;
import physis.core.iterator.DigitalOrganismIterator;
import physis.log.Log;

/**
 * This is the simplest scheduling algorithm. All organisms get the same number of cpu cycles. (This encourages code-shrinking.)
 * (this scheduling was used only in early development phase)
 */
public class ConstantScheduler implements Scheduler {
    
    DigitalOrganismIterator orgs;
    
    public void scheduleUpdate(int average){
        try{
            orgs.reset();
            
            DigitalOrganism digorg = null;
            while (orgs.hasNext()){
                digorg = orgs.next();
                if (digorg.isAlive()) {
                    digorg.update(average);
                }
            }
        } catch(Exception e){
            Log.error(e.getMessage() + " in ConstantScheduler");
        }
    }
    
    public void fillWithOrganisms(DigitalOrganismIterator orgs_){
        orgs = orgs_;
    }
    
}

