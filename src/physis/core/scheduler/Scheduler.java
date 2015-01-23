/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Scheduler.java,v 1.4 2000/06/19 15:04:44 sirna Exp $
 * $Revision: 1.4 $
 * $Date: 2000/06/19 15:04:44 $
 */
package physis.core.scheduler;

import physis.core.iterator.DigitalOrganismIterator;
/**
 * The digital organisms live together in the same space and in the same time. They're acting parallel. Now we have serial computers, so we have to simulate them.
 * <br>
 * All the classes that implement this interface wrap a scheduling algorithm.
 */
public interface Scheduler {
    /**
     * It schedules the executing of each digital organisms. The problem: we have to emulate the parallel processing of the virtual processors, and we have to take care the different working speeds.
     *  <br>
     *
     * @param average_time_slice the average number of executed instructions/per unit.
     */
    void scheduleUpdate(int average_time_slice);

    /**
     * Before starting the scheduling process the scheduler has to build up its internal data structure. The parameter is an iterator of the whole population.
     */
    void fillWithOrganisms(DigitalOrganismIterator organisms);
}
