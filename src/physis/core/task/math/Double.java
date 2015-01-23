/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Double.java,v 1.3 2001/07/06 08:23:28 sirna Exp $
 * $Revision: 1.3 $
 * $Date: 2001/07/06 08:23:28 $
 */
package physis.core.task.math;

import  physis.core.task.TaskAdapter;

public class Double extends TaskAdapter{

    public boolean checkActivity(int[] in, int[] out){
        return 2 * in[1] == out[1];
    }
    
    public int getInputSize() { return 1;}
    public int getOutputSize() { return 1;}

}

