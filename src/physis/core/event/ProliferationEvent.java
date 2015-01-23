/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: ProliferationEvent.java,v 1.4 2001/07/06 08:41:21 sirna Exp $
 * $Revision: 1.4 $
 * $Date: 2001/07/06 08:41:21 $
 */
package physis.core.event;

import java.util.EventObject;
import physis.core.virtualmachine.GeneticCodeTape;
import physis.core.DigitalOrganism;

/**
 * When an organism divides this event is generated.
 */
public class ProliferationEvent extends BiologicalEvent {
    /**
     * The genetic information of the newborn child on a codetape.
     */
    public GeneticCodeTape getNewSeed() {
	return newSeed;
    }
    
    public ProliferationEvent(GeneticCodeTape newseed, DigitalOrganism digorg){
        newSeed = newseed;
	organism = digorg;
    }

    /**
     * If the event instance is shared because of optimization considerations then we need a method to refill the values.
     */
    public void reFill(GeneticCodeTape newseed, DigitalOrganism digorg){
        newSeed = newseed;
	organism = digorg;
    }
    
    /**
     * The new codetape.
     */
    private GeneticCodeTape newSeed;
}
