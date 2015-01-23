/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Lattice2DLifeSpace.java,v 1.22 2001/07/06 08:44:26 sirna Exp $
 * $Revision: 1.22 $
 * $Date: 2001/07/06 08:44:26 $
 */
package physis.core.lifespace;

import physis.core.virtualmachine.*;
import physis.core.*;
import physis.core.iterator.*;
import physis.core.random.Randomness;

import physis.log.Log;

import java.util.*;

/**
 * dependency: Array2DDigitalOrganismIterator, ArrayDigitalOrganismIterator
 */

public class Lattice2DLifeSpace implements CellLifeSpace{
    private static final String X_SIZE="2Dlattice_xsize";
    private static final String Y_SIZE="2Dlattice_ysize";
    
    private int x_size;
    private int y_size;
    private DigitalOrganism[][] lifespace;
    private DigitalOrganism[] neighbours = new DigitalOrganism[8];
    private ArrayDigitalOrganismIterator neighbour_iterator = new ArrayDigitalOrganismIterator(neighbours);
    private Randomness rnd = PHYSIS.environment.getRandomness();
    
    /**
     *  Creates a lifespace. The dimensional parameters are got from Configuration.
     */
    public Lattice2DLifeSpace(){
        this(Integer.parseInt(Configuration.getProperty(X_SIZE)),
             Integer.parseInt(Configuration.getProperty(Y_SIZE)));
    }
    
    
    /**
     *  Creates a lifespace with different with dimension x * y.
     */
    public Lattice2DLifeSpace(int x_size_, int y_size_){
        try{
            x_size = x_size_;
            y_size = y_size_;
            lifespace = new DigitalOrganism[x_size][];
            for (int i = 0; i < lifespace.length; i++){
                lifespace[i] = new DigitalOrganism[y_size];
                for (int j = 0; j < lifespace[i].length; j++){
                    lifespace[i][j] = new DigitalOrganismImpl();
                    lifespace[i][j].setPositionInfo(new PositionInfo2DLattice(i,j));
                }
            }
        } catch(Exception e){
            Log.error(e);
        }
    }
    
    /**
     * Injects the genome into the soup. The target cell is random.
     */
    public void injectGenome(GeneticCodeTape ct){
        try{
            int x_position = rnd.nextInt(lifespace.length);
            int y_position = rnd.nextInt(lifespace[0].length);
            
            lifespace[x_position][y_position].vivify((getNeighbours(lifespace[x_position][y_position])).randomly(),ct, Configuration.getAverageTimeSlice());
        } catch(Exception e){
            Log.error(e);
        }
    }
    
    /**
     * This method provides all the organisms of the lifespace. This feature is needed  when making the update.
     */
    public DigitalOrganismIterator getAllOrganisms(){
        return new Array2DDigitalOrganismIterator(lifespace);
    }
    /**
     * This method gives the neighbours of an organism. This is needed when an organism interact with its neighbourhood. Here the
     * topology is 8-neighbourhood on a toroidal two dimensional lattice.
     */
    public DigitalOrganismIterator getNeighbours(DigitalOrganism digorg){
        try{
            PositionInfo2DLattice pos = (PositionInfo2DLattice)digorg.getPositinInfo();
            
            neighbours[0] = lifespace[(((pos.x-1) >= 0 )? (pos.x-1) : (x_size -1))] [(((pos.y-1) >= 0) ? (pos.y-1) : (y_size -1) )];
            neighbours[1] = lifespace[pos.x] [(((pos.y-1) >= 0) ? (pos.y-1) : (y_size -1) )];
            neighbours[2] = lifespace[(((pos.x+1) < x_size )? (pos.x+1) : 0 )] [(((pos.y-1) >= 0) ? (pos.y-1) : (y_size -1) )];
            neighbours[3] = lifespace[(((pos.x-1) >= 0 )? (pos.x-1) : (x_size -1))] [pos.y];
            neighbours[4] = lifespace[(((pos.x+1) < x_size )? (pos.x+1) : (0))] [pos.y];
            neighbours[5] = lifespace[(((pos.x-1) >= 0 )? (pos.x-1) : (x_size -1))] [(((pos.y+1) < y_size) ? (pos.y+1) : (0) )];
            neighbours[6] = lifespace[pos.x] [(((pos.y+1) < y_size) ? (pos.y+1) : (0) )];
            neighbours[7] = lifespace[(((pos.x+1) < x_size )? (pos.x+1) : (0))] [(((pos.y+1) < y_size) ? (pos.y+1) : (0) )];
            
            /* instead of creating all the time the iterator
             * we can kepp only one instance and change its inner fields
             * it's really weird  from the view of OO design
             * but it's really it's a significant performance gain - that's life
             * It may not cause concurrency problems, as if we run as one thread.
             */
            neighbour_iterator.reset();
        } catch(Exception e){
            Log.error(e);
        }
        return neighbour_iterator;
    }
    /**
     * Gives an organism randomly from the entire 'soup'. It's needed for point mutations for example.
     */
    public DigitalOrganism getOrganismRandomly(){
        return lifespace[rnd.nextInt(x_size)][rnd.nextInt(y_size)];
    }
    /**
     * Gives the capacity of the lifespace.
     */
    
    public int getSize(){
        return x_size * y_size;
    }
    
    public DigitalOrganismIterator getSomeOrganisms(double percentage){
	int n = (int) Math.round(getSize() * percentage);
        DigitalOrganism[] orgs = new DigitalOrganism[n];
	int i = 0;
	while (i < n){
	    orgs[i++] = getOrganismRandomly();
	}
	return new ArrayDigitalOrganismIterator(orgs);
    }
    
    public DigitalOrganismIterator getRegion(double percentage){
	return null;
    }
    
}
