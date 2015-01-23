/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Environment.java,v 1.21 2001/07/06 08:56:33 sirna Exp $
 * $Revision: 1.21 $
 * $Date: 2001/07/06 08:56:33 $
 */


package physis.core;

import physis.core.event.*;
import physis.core.nursing.Nurse;
import physis.core.nursing.NurseFactory;
import physis.core.lifespace.LifeSpace;
import physis.core.iterator.DigitalOrganismIterator;
import physis.core.genotype.GeneBank;
import physis.core.task.TaskLibrary;
import physis.log.Log;

import physis.core.random.Randomness;

/**
 * The environment of an organism is defined by the surrounding physical world - in Physis: the tasks and the other organisms.
 * CRC
 * <br>
 * 1. handling the interactions between the organism and the environment - InteractionEventListener, task
 * <br>
 * 2. handling the placement (killing) when birth of new organism - ProliferationEventListener
 * <br>
 * 3. handling the interactions between organisms (parasitism, )
 * <br>
 * 4. proxy for random number generation
 */
public interface Environment extends InteractionEventListener, ProliferationEventListener {
    /**
     * The container of the tasks.
     */
    TaskLibrary getTaskLibrary();
    
     
    void setLifeSpace(LifeSpace lifespace_);
    LifeSpace getLifeSpace();
    
    /**
     * This provides the input from the environment (~food).
     */
    int getInputData();
    
    
    void rotateForward(DigitalOrganism digorg);
    
    /**
     * Answers the question: Should the current copied instruction be mutated?
     */
    boolean copyShouldBeMutated();
    
    /**
     * Answers the question: Should the current divided codetape be mutated?
     */
    boolean divideShouldBeMutated();
    
    /**
     * Answers the question: Should the current divided codetape be inserted extra instructions?
     */
    boolean shouldBeInserted();
    
    /**
     * Answers the question: Should the current divided codetape be deleted one instruction?
     */
    boolean shouldBeDeleted();
    
    /**
     * Returns one organism of the neighbours of the organism specified by the parameter.
     */
    DigitalOrganism getNeighbourRandomly(DigitalOrganism digorg);
    
    /**
     * The only one random-generator in one Physis-instance should be in the environment.
     * There shouldn't be more than one, otherwise accurate experiment repeat would be impossible.
     */
    Randomness getRandomness();
}











