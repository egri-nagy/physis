/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Square.java,v 1.2 2001/07/06 08:23:28 sirna Exp $
 * $Revision: 1.2 $
 * $Date: 2001/07/06 08:23:28 $
 */
package physis.core.task.math;

import  physis.core.task.TaskAdapter;

public class Square extends TaskAdapter{

    public boolean checkActivity(int[] in, int[] out){
        return (in[1] > 1) && (in[1] * in[1] == out[1]);
    }

    public int getInputSize() { return 1;}
    public int getOutputSize() { return 1;}

}

