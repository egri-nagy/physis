/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: TriggerFactory.java,v 1.1 2000/09/12 07:43:34 sirna Exp $
 * $Revision: 1.1 $
 * $Date: 2000/09/12 07:43:34 $
 */
package physis.core.triggers;

import physis.log.Log;

import java.util.StringTokenizer;

public class TriggerFactory{
    
    /**
     * Creating a trigger consists of two steps:
     * <BR>
     * 1. Instantiating the object. The first token of the trigger_data is the name of the class without
     * the physis.core.trigger. prefix.
     * <BR>
     * 2. Initializing the trigger means setting the parameters (the object's fields). The trigger object
     * should parse the second part of the trigger_data String.
     */
    public static Trigger getTrigger(String trigger_data){
	Trigger trigger = null;
	StringTokenizer st = new StringTokenizer(trigger_data);
	String class_name = null;
	try{
	    class_name = "physis.core.triggers." + st.nextToken() + "Trigger";
	    Class trigger_class= Class.forName(class_name);
	    trigger = (Trigger) trigger_class.newInstance();
	    trigger.initialize(st);
        } catch (ClassNotFoundException cnfe) {
	    Log.error("Trigger class not found: " + class_name);
        }
        catch (IllegalAccessException iae) {
	    Log.error("Class or zero-argument constructor not available: " + class_name);
        }
        catch (InstantiationException cnfe) {
	    Log.error("Cannot instantiate: " + class_name);
        }
	
	return trigger;
    }
    
}
