/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Triggers.java,v 1.4 2001/08/09 10:57:08 sirna Exp $
 * $Revision: 1.4 $
 * $Date: 2001/08/09 10:57:08 $
 */
package physis.core.triggers;

import physis.core.Configuration;
import physis.log.Log;

import java.io.*;
import java.util.*;

/**
 * This class represents the main container of the triggers. In each updates it checks whether there's a trigger to be executed.
 * <br>
 * After executing it can be removed if it's not periodical or with other words its next time is not in the future.
 * <br>
 * (It might have some mechanism to load triggers on the fly, because due performance considerations there can be some tricky triggers, which load other triggers.)
 * @stereotype container
 */
public class Triggers {
    /** The container array. We use this typed array because of performance considerations. */
    Trigger[] triggers = new Trigger[0];
    
    public static final String TRIGGER_FILE = "trigger_file";
    
    /** The constructor loads the first trigger file. The filename comes from the Configuration. */
    public Triggers(){
        loadTriggers(Configuration.getPhysisHome() + Configuration.getProperty(TRIGGER_FILE));
    }
    
    /**
     * Checks the triggers in the container if they have the propper updatecount to activate. This is
     * called in every update cycle.
     */
    public void processActualTriggers(long update_count) {
	for (int i = 0; i < triggers.length; i++){
	    if (triggers[i].shouldBeExecuted(update_count)){
	        triggers[i].execute();
		Log.status("update " + update_count + ". executed trigger: " + triggers[i]);
		if (triggers[i].shouldBeRemoved()){
		    removeTrigger(i);
		}
	    }
	    
	}
    }
    
    /** Adds a trigger to the container. */
    private void addTrigger(Trigger trigger){
	Trigger[] new_triggers = new Trigger[triggers.length + 1];
	System.arraycopy(triggers, 0 , new_triggers, 0, triggers.length);
	new_triggers[new_triggers.length - 1] = trigger;
	triggers = new_triggers;
	Log.status("Added trigger: " + trigger);
    }
    
    /**
     * Removes a trigger from the indexth position.
     */
    private void removeTrigger(int index){
	Trigger[] new_triggers = new Trigger[triggers.length - 1];
	for (int i = 0, j = 0; i < triggers.length; i ++){
	    if (i != index){
	        new_triggers[j] = triggers[i];
		j++;
	    }
	}
	Log.status("Trigger removed: " + triggers[index]);
	triggers = new_triggers;
    }
    
    /** Loads the triggers from a file. One line for a trigger and there can be comments
        starting with a hashmark. */
    private void loadTriggers(String filename){
	try{
	    Log.status("Loading triggers from file: " + filename);
	    BufferedReader br = new BufferedReader(new FileReader(filename));
	    String buffer = null;
	    while ((buffer = br.readLine()) != null){
		if (!buffer.trim().startsWith("#")){
		    addTrigger(TriggerFactory.getTrigger(buffer));
		}
	    }
	}catch(Exception  e){
	    Log.error(e);
	}
	
    }
}
