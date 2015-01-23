/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: EnvironmentTrigger.java,v 1.2 2000/09/19 16:15:37 sirna Exp $
 * $Revision: 1.2 $
 * $Date: 2000/09/19 16:15:37 $
 */
package physis.core.triggers;

import physis.core.*;

/**
 * An environment-trigger can alter the environment of the population during the running.
 * <br>
 * For example killing some organisms in the population or inserting a new genotype.
 */
public class EnvironmentTrigger extends TriggerImpl {
    /**
     * Populationtriggers should access the environment, that's why they have the reference.
     */
    protected Environment environment;
    
}
