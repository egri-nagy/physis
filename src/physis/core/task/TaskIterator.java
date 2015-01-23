/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: TaskIterator.java,v 1.1 2000/05/07 17:45:24 sirna Exp $
 * $Revision: 1.1 $
 * $Date: 2000/05/07 17:45:24 $
 */
package physis.core.task;

import physis.core.DigitalOrganism;
// we need the environment to get random numbers
import physis.core.Environment;

public class TaskIterator{
    Task[] tasks = null;
    int x;
    
    
    public TaskIterator(Task[] tasks_){
        tasks = tasks_;
        x = 0;
    }
    
    public boolean hasNext(){ return x < (tasks.length);}
    
    public Task next(){
        return tasks[x++];
    }

    
    public void reset() { x  = 0; }



}
