/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: DataExtractTrigger.java,v 1.1 2000/09/19 16:15:37 sirna Exp $
 * $Revision: 1.1 $
 * $Date: 2000/09/19 16:15:37 $
 */
package physis.core.triggers;

import physis.core.*;

/**
 * Subclasses can extract some specific information about the population.
 */
public class DataExtractTrigger extends TriggerImpl {
    /**
     * Extraact-triggers should access the population, that's why they have the reference.
     */
    protected Population population;
    
}
