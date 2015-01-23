/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Storage.java,v 1.4 2003/05/08 12:24:54 sirna Exp $
 * $Revision: 1.4 $
 * $Date: 2003/05/08 12:24:54 $
 */
package arche;

/**
 * The abstraction of a processor's hardware structural elements. Storages for
 * storing values somehow (regs, stacks, queues).
 */
abstract public class Storage {
    //THE CONCEPTUAL METHODS
    public abstract short read();
    public abstract void write(short data);
    public abstract void clear();

    
    //THE TECHNICAL METHODS
    /**
     * Returns the number of cells inside the storage. It's
     * 1 for registers.
     */
    public abstract int getSize();

    /**
     * Returns structural information
     */
    public abstract String getStructure();
    
}
