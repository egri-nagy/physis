/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: StorageFactory.java,v 1.4 2002/10/10 17:55:31 sirna Exp $
 * $Revision: 1.4 $
 * $Date: 2002/10/10 17:55:31 $
 */
package arche;

public class StorageFactory {
    public static Register createRegister() {
        return new Register();
    }

    public static Storage createStack(int size) {
	    //if the size is 1 then it's a normal register
	    if (size == 1){
	      return new Register();
	    }
	return new Stack(size);
    }

    public static Storage createQueue(int size) {
	    //if the size is 1 then it's a normal register
	    if (size == 1){
	      return new Register();
	    }
	return new Queue(size);
    }

    public static InstructionPointer createInstructionPointer() {
	return new InstructionPointer();
    }

}
