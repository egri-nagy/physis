/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: PerformedTasksRegister.java,v 1.1 2000/10/09 16:21:03 sirna Exp $
 * $Revision: 1.1 $
 * $Date: 2000/10/09 16:21:03 $
 */

package physis.core.task;

import physis.core.PHYSIS;

public class PerformedTasksRegister{
    public final static byte IT_DOESNT_COUNT_ANYMORE = Byte.MAX_VALUE;
    
    /**
     * The TaskLibrary can directly access this table, that's why it has package visibility.
     * The Task IDs are the indexes and the values count how many times the tasks were performed.
     */
    byte[] table;
    
    public PerformedTasksRegister(int number_of_tasks_to_be_registered){
        table = new byte[number_of_tasks_to_be_registered];
    }
    
    public void clear(){
	for (int i = 0; i < table.length; i++){
	    table[i] = 0;
	}
    }
    
    public String toString(){
	StringBuffer sb = new StringBuffer();
	TaskLibrary tl = PHYSIS.environment.getTaskLibrary();
	for (int i = 0; i < table.length; i++){
	    if (table[i] != 0){
		sb.append("#Task " + tl.getTaskNameByID(i) + " is performed " + ( table[i] == IT_DOESNT_COUNT_ANYMORE ? " as many as possible " : (table[i] + "")) + " time(s).\n");
	    }
	}
	return sb.toString();
	
    }
}
