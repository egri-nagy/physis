/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Nurse.java,v 1.6 2000/09/19 15:59:41 sirna Exp $
 * $Revision: 1.6 $
 * $Date: 2000/09/19 15:59:41 $
 */

package physis.core.nursing;

import physis.core.DigitalOrganism;
import physis.core.iterator.DigitalOrganismIterator;
import physis.core.virtualmachine.GeneticCodeTape;

/**
 *  A new digital organism has to be placed somewhere in the lifespace.
 *  Classes implementing this interface can perform this task.
 */
public interface Nurse {
    /**
     * A new organism should be placed somewhere in the parent's neighbourhood.
     * @param newborn The newly created organism.
     * @param oldorgs The neighbourhood of the parent organism.
     * @param inherited_length The parent's effective length. The newborns use this in their first update.
     */
    void placeNewBorn(GeneticCodeTape newborn, DigitalOrganismIterator oldorgs,int inherited_length);
}
