/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Sort.java,v 1.3 2003/02/19 15:32:02 sirna Exp $
 * $Revision: 1.3 $
 * $Date: 2003/02/19 15:32:02 $
 */
package physis.core.task.advanced;

import physis.core.task.TaskAdapter;
//import physis.util.HandyMethods; 

public class Sort extends TaskAdapter{

    /** needed for comparison of i/o vectors. Do they contain the same numbers? */
    private static boolean[] isfree = new boolean[3];

    public boolean checkActivity(int[] in, int[] out){
	for (int i = 0 ; i < 3; i++){
	    isfree[i] = true;
	}

	//System.out.println("in" + HandyMethods.prettyPrintIntArray(in));
	//System.out.println("out" + HandyMethods.prettyPrintIntArray(out));
	//System.out.println("isfree after ini" + HandyMethods.prettyPrintIntArray(isfree));

	for (int i = 0 ; i < 3; i++){
	    for (int j = 0 ; j < 3; j++){
		if (isfree[j] && (in[j] == out[i])){
		    isfree[j] = false;
		    break;
		}
	    }
	}

	//System.out.println("isfree after comparison" + HandyMethods.prettyPrintIntArray(isfree));

	//if not the same numbers are in the output
	for (int i = 0 ; i < 3; i++){
	    if (isfree[i]){ return false;}
	}

	//System.out.println("isfree" + HandyMethods.prettyPrintIntArray(isfree));
        return (out[1] <= out[2]) && (out[2] <= out[3]);
    }
    
    public int getInputSize() { return 3;}
    public int getOutputSize() { return 3;}

}

