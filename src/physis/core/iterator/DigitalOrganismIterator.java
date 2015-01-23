/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: DigitalOrganismIterator.java,v 1.3 2000/10/10 16:12:50 sirna Exp $
 * $Revision: 1.3 $
 * $Date: 2000/10/10 16:12:50 $
 */
package physis.core.iterator;

import physis.core.DigitalOrganism;

/**
 * Iterator for digital organisms. It's a bit more efficient to use instead of the general Iterator, because
 * the casting is expensive.
 */
public interface DigitalOrganismIterator{
    
    
    /**
     * Returns true if there is at least one organism more.
     */
    boolean hasNext();
    
    /**
     * Returns the next DigitalOrganism from this collection.
     */
    DigitalOrganism next();
    
    /**
     * Returns true if there is at least one organism before the current.
     */
    boolean hasPrevious();
    
    /**
     * Returns the previous DigitalOrganism from this collection.
     */
    DigitalOrganism previous();
    
    /**
     * Returns the first organism.
     */
    DigitalOrganism getFirst();
    
    /**
     * Returns the last organism.
     */
    DigitalOrganism getLast();
    
    /**
     * Returns one organism from the underlying collection. It doesn't change the actual enumeration.
     */
    DigitalOrganism randomly();
    
    /**
     * Resets the iterator: the enumerating of organisms can be repeated from the begining..
     */
    void reset();
}
