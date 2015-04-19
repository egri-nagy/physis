/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: DigitalOrganism.java,v 1.28 2003/03/05 11:30:15 sirna Exp $
 * $Revision: 1.28 $
 * $Date: 2003/03/05 11:30:15 $
 */
package physis.core;

import physis.core.virtualmachine.*;
import physis.core.genotype.Genome;
import physis.core.task.PerformedTasksRegister;

/**
 * A digital organisms are the most important entities in this system. The main aim is to make
 * or let them evolve to smarter and smarter. Smarter means self-replicate and performe computational tasks quickly.
 * <BR>
 * Technically it's a virtual machine plus a codetape. The virtual machine executes the instructions of the codetapes.
 * By the execution of the code it can replicate itself, but this replication process is noisy, so the replication process can yield new codes.
 * This is the main engine of the evolution in this system. Hopefully experiments carried out by Physis will
 * answer the question: Is this enough to create diversity?
 * <BR>
 * CRC
 * <BR>
 * 1. It serves as a frame containing the organism's parts: Metabolism, Merit, virtualmachine.
 * <BR>
 */
public interface DigitalOrganism {
    
    /**
     * Makes the organism to be alive. (Fills the vital parts.)
     * This operation should entirely reset the organism's state.
     * @param neighbour_ The organism to which this is facing.
     * @param codetape The new seed.
     * @param inherited_length The newly born organism inherits the parent's effective length. (This is a trick:
     * the newly born organism should have some reasonable value in order to survive. In the next update after it's born
     * the effective length is recalculated.
     */
    void vivify(DigitalOrganism neighbour_,GeneticCodeTape codetape,int inherited_length);
    
    /**
     * Kills the organism. Technically this means only setting a flag to false. The vivifyy method takes
     * the responsibility to bring the organism into a consistent state.
     */
    void kill();
    
    /**
     * Increments the age by 1. Ususally it should happen after an update.
     */
    void increaseAge();
    
    /**
     * Gives the age of the organism.
     */
    int getAge();
    
    /**
     * Tells whether the organism is alive or not.
     */
    boolean isAlive();
    
    /**
     * Tells whether the organism is fertile (capable to yield a child).
     */
    boolean isFertile();
    
    /**
     * Sets the fertility flag.
     */
    void setFertile(boolean fertility);
    
    /**
     * Gives the genetic information of the organism.
     */
    Genome getGenome();
    
    /**
     * Gives the Metabolism of the organism.
     */
    Metabolism getMetabolism();
    
    /**
     * Gives the container of the performed tasks.
     */
    PerformedTasksRegister getPerformedTasks();
    
    /**
     * Updates the organism's state by executing one cpu-cycle.
     */
    void update();
    
    /**
     * Updates the organism's state by executing given amount of cpu_cycles.
     */
    void update(int cpu_cycles);
    
    /**
     * Returns the organism's internal virtual machine.
     */
    PhysisVirtualMachine getVM();
    
    /**
     * Returns the fitness value of the organism.
     */
    double getFitness();
    
    /**
     * Recalculates the fitness. Should be callled after successfull divede (by the Environment)
     */
    void recalculateFitness();
    
    /**
     * Recalculates the organism's bonus by multiplying it with the bonus multiplier.
     */
    void recalculateBonus(double bonus_multiplier);
    
    /**
     * Returns the current value of the bonus-multiplier.
     */
    double getBonusMultiplier();

    /**
     * Returns the effective length.
     */
    int getEffectiveLength();
    
    /**
     * Recalculates the effective length.
     */
    void recalculateEffectiveLength();
    
    /**
     * Retruns the merit.
     */
    int getMerit();
    
    /**
     * Sets the organism neighbour.
     */
    void setNeighbour(DigitalOrganism digorg);

    /**
     * Returns the organism's neighbour.
     */
    DigitalOrganism getNeighbour();
    
    /**
     * Sets the position information. The actual type of the info are is determined by the
     */
    void setPositionInfo(Object o);

    /**
     * Gives the position information.
     */
    Object getPositinInfo();
}















