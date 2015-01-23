/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: LifeSpace.java,v 1.8 2001/05/31 07:40:01 sirna Exp $
 * $Revision: 1.8 $
 * $Date: 2001/05/31 07:40:01 $
 */
package physis.core.lifespace;

import physis.core.DigitalOrganism;
import physis.core.iterator.DigitalOrganismIterator;
import physis.core.virtualmachine.GeneticCodeTape;

/**
 * LifeSpace is the space where the digital organisms live. Just like the earth for animals in real world.
 * It has to know two things. It stores the organisms and ensures the access to those. The neighbour-access methods always corrrespond to a certain topology,
 * which is hidden in the implementation. For example the lifespace can be a two-dimensional toroidal lattice
 * or a three-dimensional cube or even a one dimensional ring. This class can hide the distributed processing as well.
 * (Two LifeSpace-s can be connected across the network.)
 * <br>
 * CRC
 * <br>
 * 1. Stores the digital organisms.
 * <br>
 * 2. Handles the topolgy of the lifespace - DigitalOrganism.
 * @stereotype container
 */
public interface LifeSpace {

    /**
     * This method provides all the organisms of the lifespace. This feature is needed  when making the update.
     * The order of the organisms in the iterator is arbitrary but it must remain the same all the
     * time for one particular implementation. (Should return always a new iterator.)
     */
    DigitalOrganismIterator getAllOrganisms();
    
    /**
     * Returns some organisms.
     * The organisms should be  distributed uniformly in the whole lifespace. (Should return always a new iterator.)
     * @param percentage The percentage of organisms to be returned.
     */
    DigitalOrganismIterator getSomeOrganisms(double percentage);

    /**
     * Returns organisms from a connected local territory in the lifespace. (Should return always a new iterator.)
     * @param percentage The percentage of organisms to be returned.
     */
    DigitalOrganismIterator getRegion(double percentage);

    
    /**
     * Gives an organism randomly from the entire 'soup'. It's needed for point mutations for example.
     * The distribution of the hits should be uniform.
     */
    DigitalOrganism getOrganismRandomly();

    /**
     * This method gives the neighbours of an organism. This is needed when an organism interact with its neighbourhood.
     * The order is arbitrary. (It may return always one instance of iterator. For performance reasons.)
     */
    DigitalOrganismIterator getNeighbours(DigitalOrganism digorg);
    
    
    /**
     * Gives the capacity (the number of cells) of the lifespace.
     */
    int getSize();
    
    /**
     * Inserts a genome into the soup. The position is implementation dependent.
     */
    void injectGenome(GeneticCodeTape ct);

    
}
