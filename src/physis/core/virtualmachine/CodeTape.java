/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: CodeTape.java,v 1.14 2001/06/13 07:34:16 sirna Exp $
 * $Revision: 1.14 $
 * $Date: 2001/06/13 07:34:16 $
 */
package physis.core.virtualmachine;

/**
 * Simply represents a codetape which has a size (number of contained instructions, length)
 * and one instruction(code) can be retrieved via giving its position.
 * <BR>
 * It can be viewed as an array of short-s.
 */
public interface CodeTape{
    
    /** Returns the instruction on a given position of the tape. ALWAYS RETURNS SOME VALUE!
     *  It should handle the exceptions (index out of bounds...)
     */
    short read(int position);

    /**
     * Sets the content of the tape on the specified position. ALWAYS DOES SOMETHING!
     * There shouldn't be any unhandled error situation!
     */
    void write(int position, short value);
    
    /** Returns the number of instructions on the tape. This equals to the length. */
    int getSize();
}
