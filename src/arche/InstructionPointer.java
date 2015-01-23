/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: InstructionPointer.java,v 1.3 2003/05/08 12:24:53 sirna Exp $
 * $Revision: 1.3 $
 * $Date: 2003/05/08 12:24:53 $
 */

package arche;

/**
 * Special register for the instruction pointer. Due to the frequent accesses it provides
 * a member access for the register as well as the standard Storage access. (It's the 0th
 * structural element.)
 */
public class InstructionPointer extends Storage {
    /** For stroing the value. */
    short value;
    
    /** Returns the size of the storage element, constant 1 for a register. */
    public int getSize() { return 1; } //as this is for registers
    /** Returns the value stored in this register. */
    public short read(){ return value;}
    /** Sets the content to a new value. */
    public void write(short newvalue){ value = newvalue; }
    public void clear(){value = 0;}
    
    public String toString(){return "instructionpointer: " + value;}
    public String getStructure(){ return "I" ; }
}
