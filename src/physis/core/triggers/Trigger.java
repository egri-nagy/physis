/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Trigger.java,v 1.3 2000/09/19 16:15:37 sirna Exp $
 * $Revision: 1.3 $
 * $Date: 2000/09/19 16:15:37 $
 */

package physis.core.triggers;

import java.util.StringTokenizer;

/**
 * A trigger is a predefined event which is performed at given time in the system and/or if some conditions occur.
 * <br>
 * The trigger's parameters are read from a file. One line contains all needed parameters for a triggers. The standard
 * and obligatory parameters are the following: <br>
 * [trigger_name - without the package pre and Trigger postfix] periodical [start] [end] [step] ... <br>
 * or <BR>
 * [trigger_name - without the package pre and Trigger postfix] simple [start] ... <br>
 */
public interface Trigger {
    /**
     * Returns true if the trigger should be executed.
     * @param update_count The number of already performed updates. A trigger may ignore this information if
     * its execution doesn't depend on time but some other conditions.
     */
    boolean shouldBeExecuted(long update_count);

    /**
     * Returns true if the trigger should be removed from the active triggers-container.
     * This should be checked after execution.
     */
    boolean shouldBeRemoved();

    /**
     * Executes the trigger.
     */
    void execute();

    /**
     * Initializes the common parameters. (related to periodicity and scheduling)
     */
    void initialize(StringTokenizer parameters);

    
    /**
     * The specific initialization parameters are in a string separated by whitespaces. The parameters
     * can differ in numbers and in types thus this method is defined a very general way.
     */
    void setParameters(StringTokenizer parameters);
}
