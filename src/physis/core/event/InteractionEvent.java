/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: InteractionEvent.java,v 1.6 2001/07/06 08:41:21 sirna Exp $
 * $Revision: 1.6 $
 * $Date: 2001/07/06 08:41:21 $
 */
package physis.core.event;

import java.util.EventObject;
import physis.core.task.PerformedTasksRegister;
import physis.core.DigitalOrganism;
import physis.core.Metabolism;
import physis.log.Log;


/**
 * When the virtual machine reads data from its environment, or writes data out this event is generated.
 */
public class InteractionEvent extends BiologicalEvent {
    public InteractionEvent(DigitalOrganism digorg){
        organism = digorg;
    }

    /**
     * If the event instance is shared because of optimization considerations then we need a method to refill the values.
     */
    public void reFill(DigitalOrganism digorg){
        organism = digorg;
    }
    
    /**
     * The IObuffer of the organism-environment interaction.
     */
    public Metabolism getMetabolism() {
        try{
            
            return organism.getMetabolism();
        } catch(Exception e){
            Log.error(e);
        }
        return null;
    }
    
    /**
     * The table registering the performed tasks.
     */
    public PerformedTasksRegister getPerformedTasks(){
        try{
            return organism.getPerformedTasks();
        } catch(Exception e){
            Log.error(e);
        }
        return null;
    }
}
