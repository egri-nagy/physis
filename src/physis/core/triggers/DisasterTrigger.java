/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: DisasterTrigger.java,v 1.4 2001/05/31 07:40:45 sirna Exp $
 * $Revision: 1.4 $
 * $Date: 2001/05/31 07:40:45 $
 */
package physis.core.triggers;

import physis.core.PHYSIS;
import physis.core.DigitalOrganism;
import physis.core.iterator.DigitalOrganismIterator;

import java.util.StringTokenizer;

public class DisasterTrigger extends EnvironmentTrigger{
    double percentage;
    boolean is_regionkill;
    String type;
    
    public void setParameters(StringTokenizer params){
	percentage = Double.parseDouble(params.nextToken());
	type = params.nextToken();
	if ("region".equals(type)){
	    is_regionkill = true;
	}
	else if ("scattered".equals(type)){
	    is_regionkill = false;
	}
	
	//getting the reference for population
	environment = PHYSIS.environment;
    }
    
    public void execute(){
	DigitalOrganismIterator it = null;
	DigitalOrganism org = null;
	if (is_regionkill){
	    it = environment.getLifeSpace().getRegion(percentage);
	}
	else{
	    it = environment.getLifeSpace().getSomeOrganisms(percentage);
	}
	
	while (it.hasNext()){
	    org =  it.next();
	    if (org.isAlive()){
	        org.kill();
	    }
	}
	
	if (is_periodical){
	    adjustPeriodicalSchedule();
	}
    }
    
    public String toString(){
        return "Disaster - killing organisms. Method: " + type + " Death percentage: " + percentage;
    }
}
