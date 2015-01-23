/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: MemoryPoolLifeSpace.java,v 1.5 2001/07/06 08:44:26 sirna Exp $
 * $Revision: 1.5 $
 * $Date: 2001/07/06 08:44:26 $
 */
package physis.core.lifespace;

import physis.core.DigitalOrganism;
import physis.core.DigitalOrganismImpl;
import physis.core.iterator.DigitalOrganismIterator;
import physis.core.iterator.ArrayDigitalOrganismIterator;
import physis.core.virtualmachine.GeneticCodeTape;
import physis.core.PHYSIS;
import physis.core.Configuration;
import physis.core.random.Randomness;


/* This should be a one big memory pool lifespace. The PositionInfo is an Integer. */
public class MemoryPoolLifeSpace implements SoupLifeSpace{
    DigitalOrganism[] orgs;
    public static final int NEIGHBOURS = 4;
    public static final String SIZE = "lifespace_size";
    
    DigitalOrganism[] neighbours = new DigitalOrganism[NEIGHBOURS];
    DigitalOrganismIterator neighbour_iterator = new ArrayDigitalOrganismIterator(neighbours);
    Randomness  rnd = PHYSIS.environment.getRandomness();
    
    public MemoryPoolLifeSpace(){
	orgs = new DigitalOrganism[Integer.parseInt(Configuration.getProperty(SIZE))];
	for (int i = 0; i < orgs.length; i++){
	    orgs[i] = new DigitalOrganismImpl();
	    orgs[i].setPositionInfo(new Integer(i));
	}
    }
    
    public DigitalOrganismIterator getAllOrganisms(){
        return new ArrayDigitalOrganismIterator(orgs);
    }
    
    public DigitalOrganismIterator getNeighbours(DigitalOrganism org){
        int pos = ((Integer) org.getPositinInfo()).intValue();
	for (int i = 0; i < NEIGHBOURS; i++){
	    neighbours[i] = orgs[(pos + i) % orgs.length];
	}
	neighbour_iterator.reset();
	return neighbour_iterator;
    }
    
    public DigitalOrganism getOrganismRandomly(){
        return orgs[rnd.nextInt(orgs.length)];
    }
    
    public void injectGenome(GeneticCodeTape ct){
	DigitalOrganism org = orgs[rnd.nextInt(orgs.length)];
	org.vivify(getNeighbours(org).randomly(), ct, Configuration.getAverageTimeSlice());
    }
    
    public int getSize(){ return orgs.length; }
    
    public DigitalOrganismIterator getRegion(double percent){
        return null;
    }
    
    public DigitalOrganismIterator getSomeOrganisms(double precent){
	return null;
    }
}
