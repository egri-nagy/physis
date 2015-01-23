/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: InteractionEventListener.java,v 1.2 2000/09/19 16:09:12 sirna Exp $
 * $Revision: 1.2 $
 * $Date: 2000/09/19 16:09:12 $
 */

package physis.core.event;

import java.util.EventListener;

/** Eventlistener for environment-organism interaction handling. */
public interface InteractionEventListener extends EventListener {
    /**
     * Performed when an organism interacts with its environment.
     */
    void interactionOccured(InteractionEvent interactionevent);
}
