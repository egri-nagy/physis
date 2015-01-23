/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: TriggerImpl.java,v 1.1 2000/09/12 07:43:34 sirna Exp $
 * $Revision: 1.1 $
 * $Date: 2000/09/12 07:43:34 $
 */

package physis.core.triggers;

import java.util.StringTokenizer;

/**
 * Provides the basic implementation for the trigger classes. (setting parameters, types)
 */
public class TriggerImpl implements Trigger{
    protected boolean is_periodical;
    protected long start;
    protected long end;
    protected long step;
    /** The update number of the next time when it should run. */
    protected long next;

    /**
     * If it's not periodical or it passed its end value then it should be removed.
     */
    public boolean shouldBeRemoved(){ return !is_periodical || next > end; }
    
    /**
     * If the current update number equals the next time then it should execute.
     */
    public boolean shouldBeExecuted(long up_date){
	return up_date == next;
    }
    
    /** This method should be overridden by the concrete implementations. */
    public void execute(){
    }
    
    /**
     *  This method shoulb be invoked after execution if the trigger is periodical.
     */
    protected void adjustPeriodicalSchedule(){
        next += step;
    }
    
    /**
     * Sets the type and the scheduling information according to the type.
     */
    public void initialize(StringTokenizer params){
	
	//reading the type
	String type = params.nextToken();
	if ("periodical".equals(type)){
	    is_periodical = true;
	}
	if ("simple".equals(type)){
	    is_periodical = false;
	}
	
	//reading the schedule info
	if (is_periodical){
	    start = Long.parseLong(params.nextToken());
	    end = Long.parseLong(params.nextToken());
	    step = Long.parseLong(params.nextToken());
	}
	else {
	    start = Long.parseLong(params.nextToken());
	}
	next = start;
	
	//setting the trigger specific parameters
	setParameters(params);
    }
    
    /** Should be overridden in implementations. */
    public void setParameters(StringTokenizer st){
	//it does nothing in this general case
    }
}
