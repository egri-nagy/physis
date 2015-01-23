/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Echo.java,v 1.6 2001/07/06 08:23:28 sirna Exp $
 * $Revision: 1.6 $
 * $Date: 2001/07/06 08:23:28 $
 */
package physis.core.task.basic;

import  physis.core.task.TaskAdapter;

public class Echo extends TaskAdapter{

    public boolean checkActivity(int[] in, int[] out){
        return in[1] == out[1];
    }
    
    public int getInputSize() { return 1;}
    public int getOutputSize() { return 1;}

}

















