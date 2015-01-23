/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: StatisticsFactory.java,v 1.1 2000/09/01 09:11:39 sirna Exp $
 * $Revision: 1.1 $
 * $Date: 2000/09/01 09:11:39 $
 */
package physis.core.statistics;

import physis.core.Configuration;
import physis.core.Population;
import physis.log.*;

public class StatisticsFactory{
    public static Statistics getStatistics(Population pop){
	Statistics stat = null;
	try{
	    Log.status("Creating statistics...");
	    stat = new StatisticsImpl(pop);
	} catch (Exception e) {
	    Log.error("Couldn't create statistics");
	}
	return stat;
    }
}
