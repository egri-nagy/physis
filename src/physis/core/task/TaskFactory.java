/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: TaskFactory.java,v 1.2 2000/10/09 16:21:03 sirna Exp $
 * $Revision: 1.2 $
 * $Date: 2000/10/09 16:21:03 $
 */

package physis.core.task;

import physis.log.Log;

public class TaskFactory{
    
    private static int id_generator;
    
    public static Task getTask(String groupname, String taskname){
        Task task = null;
        try{
            
            Class task_class = Class.forName("physis.core.task." + groupname + "." +taskname);
            task  = (Task)task_class.newInstance();
	    //setting the id
	    task.setID(id_generator);
	    id_generator++;
        } catch (ClassNotFoundException cnfe) {
            Log.error("VirtualMachine class not found: " + "physis.core.task." + groupname + "." + taskname);
        }
        catch (IllegalAccessException iae) {
            Log.error("Class or zero-argument constructor not available: " + "physis.core.task." + "." + groupname + taskname);
        }
        catch (InstantiationException cnfe) {
            Log.error("Cannot instantiate: " + "physis.core.task." + groupname + "." + taskname);
        }
        
        return task;
    }
}
