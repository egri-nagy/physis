/*
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: ChangeTaskBonusesTrigger.java,v 1.1 2000/10/31 18:27:55 sirna Exp $
 * $Revision: 1.1 $
 * $Date: 2000/10/31 18:27:55 $
 */
package physis.core.triggers;

import physis.core.PHYSIS;

import java.util.StringTokenizer;

public class ChangeTaskBonusesTrigger extends EnvironmentTrigger{
    
    private int percent;
    
    public void setParameters(StringTokenizer params){
	percent = Integer.parseInt(params.nextToken());
	//getting the reference for population
	environment = PHYSIS.environment;
	
    }
    
    public void execute(){
	environment.getTaskLibrary().changeBonusesGlobally(percent);
	if (is_periodical){
	    adjustPeriodicalSchedule();
	}
    }
    
    public String toString(){
        return "Changing task bonuses" + percent;
    }
}
