/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Put.java,v 1.2 2000/10/09 16:21:04 sirna Exp $
 * $Revision: 1.2 $
 * $Date: 2000/10/09 16:21:04 $
 */
package physis.core.task.basic;

import physis.core.task.TaskAdapter;

public class Put extends TaskAdapter{
        
    public boolean checkActivity(int[] in, int[] out){
        return true;
    }
    
    public int getInputSize() { return 0;}
    public int getOutputSize() { return 1;}
}
