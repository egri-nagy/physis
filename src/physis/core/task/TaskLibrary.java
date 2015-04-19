/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: TaskLibrary.java,v 1.9 2000/10/31 18:26:55 sirna Exp $
 * $Revision: 1.9 $
 * $Date: 2000/10/31 18:26:55 $
 */

package physis.core.task;

import java.io.*;
import java.util.*;

import physis.core.*;
import physis.log.Log;

/**
 * @stereotype proxy
 */
public class TaskLibrary {
    
    /**
     * Contains all of the tasks in the order of instantiation.
     */
    private Vector alltasks;
    /**
     * Keys groupnames, values Taskarrays.
     */
    private Hashtable groups_by_names = new Hashtable();
    
    /**
     * Taskarrays are mapped by their inputsize and outputsize.
     */
    private Task[][][] task_lookup_table;
    
    /**
     *  Builds the internal datastructure of the task framework.
     *
     *
     */
    public void buildLibrary(String taskfilename){
        try{
	    BufferedReader br = new BufferedReader(
		new FileReader(taskfilename));
	    
	    // keys: groupname values: Vectors containing the tasks in the groups.
	    Hashtable temp = new Hashtable();
	    //just to have all the Vectors for further processing.
	    alltasks = new Vector();
	    int max_input_size = 0;
	    int max_output_size = 0;
	    
	    String line = br.readLine();
	    while ((line != null) && (!line.equals(""))){
		//skip if it's comment...
		if (line.startsWith("#")){
		    line = br.readLine();
		    continue;
		}
		
		
		StringTokenizer st = new StringTokenizer(line);
		String groupname = st.nextToken();
		String taskname = st.nextToken();
		
		Task task = TaskFactory.getTask(groupname, taskname);
		alltasks.add(task);
		if (task.getInputSize() > max_input_size){
		    max_input_size = task.getInputSize();
		}
		if (task.getOutputSize() > max_output_size){
		    max_output_size = task.getOutputSize();
		}
		
		Vector bonuses = new Vector();
		while (st.hasMoreTokens()){
		    bonuses.add(new Double(st.nextToken()));
		}
		
		double[] bonuses_vals = new double[bonuses.size()];
		for (int i = 0 ; i < bonuses_vals.length; i++){
		    bonuses_vals[i] = ( (Double) bonuses.elementAt(i)).doubleValue();
		}
		task.setMeritMultipliers(bonuses_vals);
		
		//na ezt lehetne szebben is!
		if (temp.containsKey(groupname)){
		    ((Vector) temp.get(groupname)).add(task);
		}
		else {
		    temp.put(groupname, new Vector());
		    ((Vector) temp.get(groupname)).add(task);
		}
		
		line = br.readLine();
		
	    }
	    
	    if (alltasks.size() > Configuration.getMaxNumberOfTasks()){
		Configuration.setMaxNumberOfTasks(alltasks.size());
		Log.status("Reset by Tasklibrary. No task insert is possible in the future!");
	    }
	    
	    //making the hashtable by groupnames
	    Enumeration groupnames = temp.keys();
	    while (groupnames.hasMoreElements()){
		String key = (String) groupnames.nextElement();
		Vector tasks = (Vector) temp.get(key);
		Task[] taskarray = new Task[tasks.size()];
		tasks.copyInto(taskarray);
		groups_by_names.put(key, taskarray);
	    }
	    
	    //creating the lookuptable
	    int max_task_size = Math.max(max_input_size + 1, max_output_size + 1);
	    task_lookup_table = new Task[max_task_size][max_task_size][];
	    Configuration.setMaxTaskSize(max_task_size - 1);
	    //filling the table
	    for (int i = 0; i < task_lookup_table.length; i++){
		for (int j = 0; j < task_lookup_table[0].length; j ++){
		    Vector tmp = new Vector();
		    Enumeration e = alltasks.elements();
		    while (e.hasMoreElements()){
			Task t = (Task) e.nextElement();
			if  ((t.getInputSize() == i) && (t.getOutputSize() == j)){
			    tmp.add(t);
			}
		    }
		    if (tmp.size() != 0){
			task_lookup_table[i][j] = new Task[tmp.size()];
			tmp.copyInto(task_lookup_table[i][j]);
		    }
		}
	    }
        } catch (IOException ioe) { Log.error(ioe);}
    }
    
    public Enumeration getTaskGroups() {
        return groups_by_names.elements();
    }
    public TaskIterator getTasksFromGroup(String groupname) {
        return new TaskIterator( (Task[]) (groups_by_names.get(groupname)));
    }
    
    /**
     * Returns the meritmultiplier according to the organism's activity or Zero if it didn't perform any task.
     */
    public double checkIOActivity(Metabolism metabolism, PerformedTasksRegister performed_tasks) {
        double meritmultiplier = 1;
	Task[] tasks = task_lookup_table[metabolism.getInputSize()] [metabolism.getOutputSize()];
	
        //no tasks in this type
        if (tasks == null) return meritmultiplier;
        
        int[] inputs = metabolism.getInputs();
        int[] outputs = metabolism.getOutputs();
        
        for (int i = 0; i < tasks.length; i++){
	    if (tasks[i].checkActivity(inputs, outputs)){
		tasks[i].increaseCounter();
		int task_id = tasks[i].getID();
		byte counter = performed_tasks.table[task_id];
		if (counter == 0){
		    meritmultiplier = tasks[i].getMeritMultiplier(counter);
		    performed_tasks.table[task_id]++;
		}
		else{
		    if (counter >= tasks[i].getNumberOfRewardedPerforms()){
			performed_tasks.table[task_id] = PerformedTasksRegister.IT_DOESNT_COUNT_ANYMORE;
		    }
		    else{
			meritmultiplier = tasks[i].getMeritMultiplier(counter);
			performed_tasks.table[task_id]++;
		    }
		}
		
		if (tasks[i].shouldBeCleared()) {
		    metabolism.clear();
		}
		return meritmultiplier;
	    }
        }
        
        return meritmultiplier;
    }
    
    public String getTaskNameByID(int id){
        return ( (Task) alltasks.elementAt(id) ).getName();
    }
   
    public void changeBonusesGlobally(int percent){
        Log.status("Changing task bonuses by " + percent + " percent");
	double multiplier = percent / 100.0;
	Iterator i = alltasks.iterator();
	while (i.hasNext()){
	    Task t = ((Task) i.next());
	    t.changeBonuses(multiplier);
	    Log.status(t.toString());
	}
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        Enumeration groups = groups_by_names.keys();
        while (groups.hasMoreElements()){
	    String groupname = (String) groups.nextElement();
	    sb.append("TASKGROUP " + groupname + "\n");
	    Task[] tasks = (Task[]) groups_by_names.get(groupname);
	    for (int i = 0; i < tasks.length; i++){
		sb.append("Task " + tasks[i].getName() + " was performed " + tasks[i].getCounter() + "\n");
	    }
	}
	return sb.toString();
    }
    
}


