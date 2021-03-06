/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Reverse.java,v 1.2 2001/07/06 08:23:28 sirna Exp $
 * $Revision: 1.2 $
 * $Date: 2001/07/06 08:23:28 $
 */
package physis.core.task.advanced;

import  physis.core.task.TaskAdapter;

public class Reverse extends TaskAdapter{
    
    public boolean checkActivity(int[] in, int[] out){
        return (in[1] == out[2]) && (in[2] == out[1]);
    }
    
    public int getInputSize() { return 2;}
    public int getOutputSize() { return 2;}
    
}

