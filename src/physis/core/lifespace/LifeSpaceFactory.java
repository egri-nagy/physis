/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: LifeSpaceFactory.java,v 1.1 2000/04/18 15:31:45 sirna Exp $
 * $Revision: 1.1 $
 * $Date: 2000/04/18 15:31:45 $
 */
package physis.core.lifespace;

import physis.log.Log;
import physis.core.Configuration;


public class LifeSpaceFactory{
    private static final String LIFESPACE_CLASS_NAME = "lifespace_class_name";
    public static LifeSpace getInstance(){
		LifeSpace ls = null;
	//instantiating the LifeSpace class
	Log.status("Trying to instantiate lifespace: " + Configuration.getProperty(LIFESPACE_CLASS_NAME));
	try{
	    
	    Class ls_class = Class.forName(Configuration.getProperty(LIFESPACE_CLASS_NAME));
	    ls = (LifeSpace)ls_class.newInstance();
	    Log.status("Lifespace size: " + ls.getSize());
	} catch (ClassNotFoundException cnfe) {
	    Log.error("LifeSpace class not found: " + Configuration.getProperty(LIFESPACE_CLASS_NAME));
	}
	catch (IllegalAccessException iae) {
	    Log.error("Class or zero-argument constructor not available: " + Configuration.getProperty(LIFESPACE_CLASS_NAME));
	}
	catch (InstantiationException cnfe) {
	    Log.error("Cannot instantiate: " + Configuration.getProperty(LIFESPACE_CLASS_NAME));
	}

	
	return ls;

    }

}