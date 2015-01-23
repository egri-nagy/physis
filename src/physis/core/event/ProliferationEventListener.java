/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: ProliferationEventListener.java,v 1.2 2000/05/14 12:52:50 sirna Exp $
 * $Revision: 1.2 $
 * $Date: 2000/05/14 12:52:50 $
 */

package physis.core.event;

import java.util.EventListener;
public interface ProliferationEventListener extends EventListener {
    /**
     * When a digital organism divides the new organism must be placed somewhere in the lifespace
     * and the old organism's effective length should be recalculated.
     * The parent organism's VM should be reset.
     * The parent orgainsm should be set fertile.
     * These should be handled in this method by the implementor classes.
     */
    void proliferationPerformed(ProliferationEvent prolevent);
}
