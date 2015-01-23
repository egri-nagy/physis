/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: TaskGroup.java,v 1.2 2000/05/07 17:45:24 sirna Exp $
 * $Revision: 1.2 $
 * $Date: 2000/05/07 17:45:24 $
 */


package physis.core.task;

import java.util.*;

public class TaskGroup {
    
    private String name;
    private TaskIterator taskiterator;
    
    public TaskGroup(String name, Vector tasks){
        this.name = name;
        Task[] tasksarray = new Task[tasks.size()];
        for (int i = 0; i < tasks.size(); i++){
            tasksarray[i] = (Task) tasks.elementAt(i);
        }
        taskiterator = new TaskIterator(tasksarray);
    }
    
    public String getGroupName() {
        return name;
    }
    
    public TaskIterator getTasks() {
        return taskiterator;
    }
    
}
