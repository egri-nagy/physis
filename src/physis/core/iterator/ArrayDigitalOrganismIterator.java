/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: ArrayDigitalOrganismIterator.java,v 1.6 2001/07/06 08:51:14 sirna Exp $
 * $Revision: 1.6 $
 * $Date: 2001/07/06 08:51:14 $
 */
package physis.core.iterator;

import physis.core.DigitalOrganism;
// we need the environment to get random numbers
import physis.core.PHYSIS;

/**
 * Enumerates organisms from an array.
 */
public class ArrayDigitalOrganismIterator implements DigitalOrganismIterator{
    DigitalOrganism[] orgs = null;
    int x;
    
    
    public ArrayDigitalOrganismIterator(DigitalOrganism[] dorgs){
        orgs = dorgs;
        x = 0;
    }
    
    public boolean hasNext(){ return x < (orgs.length);}
    
    public DigitalOrganism next(){
        return orgs[x++];
    }

    public boolean hasPrevious() { return x > 0; }
    
    public DigitalOrganism previous(){
        return orgs[x--];
    }
    
    public DigitalOrganism getFirst(){ return orgs[0]; }
    public DigitalOrganism getLast() { return orgs[orgs.length - 1]; }
    
    public DigitalOrganism randomly() {
        
        return orgs[PHYSIS.environment.getRandomness().nextInt(orgs.length)];
    }
    
    public void reset() { x  = 0; }

}
