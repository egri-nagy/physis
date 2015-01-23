/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: SchedulerFactory.java,v 1.2 2000/09/21 12:58:10 sirna Exp $
 * $Revision: 1.2 $
 * $Date: 2000/09/21 12:58:10 $
 */
package physis.core.scheduler;

import physis.core.Configuration;
import physis.log.*;

public class SchedulerFactory{
    public static final String SCHEDULER_CLASS_NAME="scheduler_class_name";
    
    public static Scheduler getScheduler(){
         String scheduler_class = Configuration.getProperty(SCHEDULER_CLASS_NAME);
         Log.status("Trying to create scheduler...");
	//instantiating the Scheduler class
	Scheduler sched = null;
	try{
	    
	    Class sched_class = Class.forName(scheduler_class);
	    sched = (Scheduler)sched_class.newInstance();
	} catch (ClassNotFoundException cnfe) {
	    Log.error("Scheduler class not found: " + scheduler_class);
	}
	catch (IllegalAccessException iae) {
	    Log.error("Class or zero-argument constructor not available: " + scheduler_class);
	}
	catch (InstantiationException cnfe) {
	    Log.error("Cannot instantiate: " + scheduler_class);
	}
	Log.status("SCHEDULER: " + scheduler_class);
	return sched;
    }
}
