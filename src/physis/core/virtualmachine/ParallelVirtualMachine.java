/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: ParallelVirtualMachine.java,v 1.1 2000/04/07 16:57:33 sirna Exp $
 * $Revision: 1.1 $
 * $Date: 2000/04/07 16:57:33 $
 */

package physis.core.virtualmachine;

/**
 * This VM can run multiple threads concurrently. The resources are shared. 
 */
abstract public class ParallelVirtualMachine extends VirtualMachine {
/**
 * @link aggregation 
 */
    private VirtualMachine[] threads;
}
