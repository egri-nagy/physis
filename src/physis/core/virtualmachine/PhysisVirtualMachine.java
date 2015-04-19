/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: PhysisVirtualMachine.java,v 1.14 2001/08/09 11:00:43 sirna Exp $
 * $Revision: 1.14 $
 * $Date: 2001/08/09 11:00:43 $
 */
package physis.core.virtualmachine;

import physis.core.DigitalOrganism;
import physis.core.genotype.Genome;

/**
 * This is the base class for all virtualmachines which are implemented in Physis.
 * This class should contain some "biological" fields and operations.
 * @author sirna
 */
public abstract class PhysisVirtualMachine extends VirtualMachine{
    /** The blank instruction. It's implicitly part of all instructionsets. */
    protected final static short blank = -1;
    
    /**
     * This codetape contains the organism's genetic code.
     */
    protected GeneticCodeTape tape;
    
    /**
     * This is another codetape for alien genomes. (for parasitism)
     */
    protected GeneticCodeTape alien;
    
    /**
     * It's a backreference to the bearer digtal organism.
     * It's useful when generating an event and there's a need to know the digital organism.
     * The same with environment interactions and parasitism or facing change.
     */
    protected DigitalOrganism bearer;
    
    /**
     * This value shows that the VM hasn't executed a succesfull divide operation yet.
     */
    public final static int GESTATION_TIME_INVALID = Integer.MAX_VALUE;
    
    /**
     * Time (in executed instructions) needed to create a child. (Actually it's the last time when birth happened.)
     */
    protected int gestation_time = GESTATION_TIME_INVALID;
    
    /**
     * counts the instructions between creating childs
     */
    protected int counter;
    
    /**
     * Gives the number of instructions that was needed last time to yield a new organism. If the organism hasn't spawned yet,
     * then it returns the number of executed instructions. This way the unfertile organisms are filtered out,
     * but the first time after birth they have good chance (low gestation time
     */
    public int getGestationTime(){
        return gestation_time;
    }
    
    /**
     *  Returns the genetic information.
     */
    public abstract Genome getGenome();
    
    /**
     * Gives the number of copied or executed instructions. (It grabs it from the CodeTape.)
     */
    public int getEffectiveLength() {
	return tape.calculateEffectiveLength();
    }
    
    /** This should be called after successfull proliferation. (it doesn't change the gestation_time to invalid) */
    public abstract void restart();
    
    
    /**
     *  Loads the codetape into the VM.
     */
    public void loadCodeTape(GeneticCodeTape ct){
        tape = ct;
    }
            
    
    /**
     *  Sets the bearer organism of the VM.
     */
    public void setBearer (DigitalOrganism bearer_) { bearer = bearer_; }
    /**
     * Returns the bearer organism.
     */
    public DigitalOrganism getBearer() { return bearer; }
    
    
}
