/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Task.java,v 1.8 2000/10/31 18:26:55 sirna Exp $
 * $Revision: 1.8 $
 * $Date: 2000/10/31 18:26:55 $
 */

package physis.core.task;

/**
 * It represents a task, more specifically a function. The arguments and resulted values are integers.
 */
public interface Task {
    
    /**
     * Returns the number of input arguments.
     */
    int getInputSize();
    /**
     * Returns the number of generated values.
     */
    int getOutputSize();
    /**
     * Checks an IO activity whether it performs the represented task.
     */
    boolean checkActivity(int[] input, int[] output);
    /**
     * Returns the merit multiplier when the task is performed.
     * @param nth The number indicating how many times the task was performed.
     */
    double getMeritMultiplier(int nth);
    
    /**
     *  Sets the meritmultipliers.
     */
    void setMeritMultipliers(double[] meritmultipliers);
    
    /**
     *  Not all executions of a task are rewarded. It's easy to repeat, but it's hard to invent.
     */
    int getNumberOfRewardedPerforms();

    /**
     *  Multiplies the bonuses via multiplying the bonus's part above 1.0.
     */
    void changeBonuses(double multiplier);
    
    /**
     * Answers the question: Should the I/O-buffer (@see Metabolism) be cleared after completing this task.
     */
    boolean shouldBeCleared();

    /**
     *  If the task is performed, the counter should be increased.
     **/
    void increaseCounter();
    
    /**
     * Gets the value of task counter. It means that the task was performed this many times.
     */
    int getCounter();
    
    /**
     * Name specification:
     *
     * groupname_idname_inputnumber_outputnumber
     */
    String getName();
    
    /**
     * Returns the internal ID of the task. This ID is set by TaskFactory.
     * The ID used int the organism's performed tasks table.
     */
    int getID();
    
    /** Sets the internal ID */
    void setID(int id);
}
