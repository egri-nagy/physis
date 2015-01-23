/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: StorageArray.java,v 1.4 2001/07/06 09:03:39 sirna Exp $
 * $Revision: 1.4 $
 * $Date: 2001/07/06 09:03:39 $
 */

package arche;

import physis.log.Log;

import java.util.Vector;
import java.util.Iterator;

/**
 * The hardware memory-array that can be divided for the structural elements.
 * The class is responsible for the initial division of the processor's internal memory.
 * It should handle exceptions such as overallocation.
 */
public class StorageArray{
    int max_size;
    int number_of_allocated_regs;
    Vector structural_elements;
    
    public StorageArray(int number_of_regs){
	max_size = number_of_regs;
	structural_elements = new Vector();
	//the first register is the IP
	structural_elements.add(StorageFactory.createInstructionPointer());
	number_of_allocated_regs = 1;
    }
    
    /**
     * Structural elements can be added step-by-step.
     */
    public void addStorage(Storage st){
        //Overallocationchecking
	if ((number_of_allocated_regs + st.getSize()) > max_size){
	    return;
	}
	else{
	    structural_elements.add(st);
	    number_of_allocated_regs += st.getSize();
	}
    }
    
    /**
     * After building-up, it gives back the array of structural elements.
     */
    Storage[] getStructuralElements(){
	        Storage[] ses = new Storage[structural_elements.size()];
	Iterator it = structural_elements.iterator();
	for (int i = 0; it.hasNext(); ses[i++] = (Storage) it.next());
	return ses;
    }
    
    public String toString(){ return "" + structural_elements;}
}

