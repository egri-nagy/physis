/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: TaskAdapter.java,v 1.4 2000/10/31 18:26:55 sirna Exp $
 * $Revision: 1.4 $
 * $Date: 2000/10/31 18:26:55 $
 */

package physis.core.task;

/**
 * Provides counting and merit-multiplier handling.Subclasses should override the
 * checkActivity, getInputSize and getOutputSize and possibly the getName method
 * and when clearing is not wanted the shouldBeCleared method.
 */
public class TaskAdapter implements Task {
    
    protected int counter = 0;
    protected double[] meritmultipliers;
    protected int id;
    
    public void setMeritMultipliers(double[] meritms) { meritmultipliers = meritms; }
    
    public double getMeritMultiplier(int nth) {
        return meritmultipliers[nth];
    }
    
    public boolean checkActivity(int[] in, int[] out){
        return false;
    }
    
    public int getInputSize() { return 0;}
    public int getOutputSize() { return 0;}
    
    public int getNumberOfRewardedPerforms() { return meritmultipliers.length; }
    
    public void changeBonuses(double multiplier){
        for (int i = 0; i < meritmultipliers.length; i++){
	    meritmultipliers[i] = meritmultipliers[i] + ((meritmultipliers[i] - 1.0) * multiplier);
	}
    }
    
    /**
     *  Clearing is the default.
     */
    public boolean shouldBeCleared() { return true; }
    
    
    public void increaseCounter(){ counter++; }
    public int getCounter() { return counter; }
    
    /** The name equals the class name without the physis.core.task. - prefix. */
    public String getName() { return this.getClass().getName().substring("physis.core.task.".length()); }
    
    public int getID(){ return id; }
    public void setID(int id_){ id = id_;}
    
    public String toString(){
        StringBuffer sb = new StringBuffer();
	sb.append(getName() + " ");
	for (int i = 0; i < meritmultipliers.length; i++){
	    sb.append(meritmultipliers[i] + " " );
	}
	return sb.toString();
    }
}
