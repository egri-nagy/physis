/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: NurseFactory.java,v 1.3 2000/09/21 12:58:10 sirna Exp $
 * $Revision: 1.3 $
 * $Date: 2000/09/21 12:58:10 $
 */


package physis.core.nursing;

import physis.log.Log;
import physis.core.Configuration;

/**
 * Creates a Nurse. The classname for the nurse comes from the  configuration file.
*/
public class NurseFactory {
    public static Nurse getNurse() {
	Nurse nurse = null;
	Log.status("Trying to instantiate Nurse...");
	try{
            
            Class nurse_class= Class.forName(Configuration.getNurseClassName());
            nurse = (Nurse) nurse_class.newInstance();
        } catch (ClassNotFoundException cnfe) {
            Log.error("Nurse class not found: " + Configuration.getNurseClassName());
        }
        catch (IllegalAccessException iae) {
            Log.error("Class or zero-argument constructor not available: " + Configuration.getNurseClassName());
        }
        catch (InstantiationException cnfe) {
            Log.error("Cannot instantiate: " + Configuration.getNurseClassName());
        }

	Log.status("NURSE: " + Configuration.getNurseClassName());
	return nurse;
	
    }
}







