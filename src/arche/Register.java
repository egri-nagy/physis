/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Register.java,v 1.4 2003/05/08 12:24:54 sirna Exp $
 * $Revision: 1.4 $
 * $Date: 2003/05/08 12:24:54 $
 */
package arche;

/**
 * Simple one-value register as a basic storage for processors.
 */
public class Register extends Storage {
    /** For stroing the value. */
    private short reg;
    
    /** Returns the size of the storage element, constant 1 for a register. */
    public int getSize() { return 1; } //as this is for registers
    /** Returns the value stored in this register. */
    public short read(){ return reg;}
    /** Sets the content to a new value. */
    public void write(short newvalue){ reg = newvalue; }
    public void clear(){reg = 0;}
    
    public String toString(){return "register: " + reg;}
    public String getStructure(){ return "R" ; }
}
