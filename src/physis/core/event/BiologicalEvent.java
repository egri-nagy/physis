/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: BiologicalEvent.java,v 1.3 2000/09/19 16:09:12 sirna Exp $
 * $Revision: 1.3 $
 * $Date: 2000/09/19 16:09:12 $
 */
package physis.core.event;

import physis.core.DigitalOrganism;

/**
 * We call some events occuring in this artificial system biological because their role is similar.
 */
abstract public class BiologicalEvent {
    /**
     * This is the source of the event. The organism whichs generates the event.
     */
    protected DigitalOrganism organism;

    /**
     * Returns the actor organism of the event.
     */
    public DigitalOrganism getOrganism() {
	return organism;
    }

}
