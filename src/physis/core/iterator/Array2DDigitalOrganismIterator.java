/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Array2DDigitalOrganismIterator.java,v 1.5 2001/07/06 08:51:14 sirna Exp $
 * $Revision: 1.5 $
 * $Date: 2001/07/06 08:51:14 $
 */
package physis.core.iterator;

import physis.core.DigitalOrganism;
// we need the random engine from the environment
import physis.core.PHYSIS;


/**
 * Enumerates organisms from a two-dimensional array.
 */
public class Array2DDigitalOrganismIterator implements DigitalOrganismIterator{
    DigitalOrganism[][] orgs = null;
    int x;
    int y;
    
    public Array2DDigitalOrganismIterator(DigitalOrganism[][] dorgs){
	orgs = dorgs;
	x = y = 0;
    }
    
    public boolean hasNext(){ return (x < (orgs.length)) && (y < orgs[0].length);}
    
    public DigitalOrganism next(){
	DigitalOrganism digorg = orgs[x][y];
        y = (y + 1) % orgs[x].length;
	if (y == 0) x++;
	return digorg;
    }

    public boolean hasPrevious(){ return (x > 0) && (y > 0); }

    public DigitalOrganism previous() {
        DigitalOrganism digorg = orgs[x][y];
	if (y > 0){
	    y--;
	}
	else{
	    x--;
	    y = orgs[x].length - 1;
	}
	return digorg;
    }
    
    public DigitalOrganism getFirst() { return orgs[0][0]; }
    public DigitalOrganism getLast() { return orgs[orgs.length - 1][orgs[orgs.length - 1].length - 1]; }
    
    
    public DigitalOrganism randomly() { return orgs[PHYSIS.environment.getRandomness().nextInt(orgs.length)][PHYSIS.environment.getRandomness().nextInt(orgs[0].length)];}
    
    public void reset() { x = y = 0; }
    
    
}
