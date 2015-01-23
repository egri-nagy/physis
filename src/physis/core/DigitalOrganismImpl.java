/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: DigitalOrganismImpl.java,v 1.12 2003/03/05 11:30:16 sirna Exp $
 * $Revision: 1.12 $
 * $Date: 2003/03/05 11:30:16 $
 */
package physis.core;

import physis.core.virtualmachine.*;
import physis.core.genotype.Genome;
import physis.core.task.PerformedTasksRegister;
import physis.log.Log;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

/**
 * Plain implementation of the DigitalOrganism interface. This is used in most cases.
 */
public class DigitalOrganismImpl implements DigitalOrganism {
    /** The age of the organism in updates. */
    protected int age;
    /** Indicates that the organism could bear at least one child */
    protected boolean isfertile;
    /** Indicates that the organism is ready to run */
    protected boolean isAlive = false;
    /** The organism's merit */
    protected Merit merit;
    /** The "heart" of the organism */
    protected PhysisVirtualMachine vm;
    /** It is actually the virtualmachine's I/O buffer. */
    protected Metabolism metabolism;
    /** One of the organism's neighbours is special because all the close interactions happen between two organism. */
    protected DigitalOrganism neighbour;
    
    protected double fitness;
    
    /**
     * This is information about the position of the organism in the lifespace. This attribute's type is defined in the corresponding LifeSpace class.
     */
    protected Object position_info;
    
    /**
     * This hashtable keeps track the performed tasks. The keys are tasknames, and the values are numbers
     * indicating how many times the was performed the task.
     */
    protected PerformedTasksRegister performed_tasks;
    
    /** When a task is performed too many times than it doesn't mean any bonus. */
    public static int IT_DOESNT_COUNT_ANY_MORE = Integer.MAX_VALUE;
    
    
    /** It simply creates the organism - it doesn't mean it's alive. */
    public DigitalOrganismImpl(){
        performed_tasks = new PerformedTasksRegister(Configuration.getMaxNumberOfTasks());
        merit = new Merit();
        metabolism = new Metabolism(Configuration.getMaxTaskSize());
        isAlive = false;
        isfertile = false;
        vm = VirtualMachineFactory.getPhysisVM();
        vm.setBearer(this);
    }
    
    public DigitalOrganismImpl(String vm_classname){
        performed_tasks = new PerformedTasksRegister(Configuration.getMaxNumberOfTasks());
        merit = new Merit();
        metabolism = new Metabolism(Configuration.getMaxTaskSize());
        isAlive = false;
        isfertile = false;
        vm = VirtualMachineFactory.getPhysisVM(vm_classname);
        vm.setBearer(this);
    }
    
    
    
    /**
     * Makes the organism to be alive. (Fills the vital parts.)
     * @param neighbour_ The organism to which this is facing.
     * @param codetape The new seed.
     * @param inherited_length The newly born organism inherits the parent's effective length.
     */
    public synchronized void vivify(DigitalOrganism neighbour_,GeneticCodeTape codetape,int inherited_length){
        //clear the possible old stuff
        performed_tasks.clear();
        
        //switch the state
        isAlive = true;
        isfertile = false;
	
        merit.setEffectiveLength(inherited_length);
        merit.setBonusMultiplier(1);
        
        metabolism.clear();
	
        vm.loadCodeTape(codetape);
	
        vm.reset();
        
        neighbour = neighbour_;
	
        //this means that organism is just born
        age = 0;
	
    }
    
    /**
     * Kills the organism.
     */
    public synchronized void kill(){
        //it's enough to set these flags only, because nothing happens with an organism if it's dead
        isAlive = false;
	isfertile = false;
    }
    
    public void increaseAge() { age++; }
    public int getAge() { return age; }
    
    public boolean isAlive() { return isAlive; }
    
    public boolean isFertile() { return isfertile; }
    public void setFertile(boolean fertility) { isfertile = fertility; }
    
    
    public PhysisVirtualMachine getVM() { return vm; }
    public Genome getGenome() { return vm.getGenome(); }
    
    public int getGenomeSize(){ return vm.getGenomeSize(); }
    
    public Metabolism getMetabolism() { return metabolism; }
    
    public PerformedTasksRegister getPerformedTasks(){ return performed_tasks; }
    
    
    public void update(){ vm.execute();}
    public void update(int cpu_cycles){vm.execute(cpu_cycles);}
    
    public double getFitness() { return fitness; }
    public void recalculateFitness() { fitness = getMerit() / (double)vm.getGestationTime(); }
    
    public void recalculateBonus(double bonus_multiplier){
        merit.multiplyBonus(bonus_multiplier);
    }

    public double getBonusMultiplier(){
	return merit.getBonusMultiplier();
    }
    
    public int getEffectiveLength() {
        return merit.getEffectiveLength();
    }
    
    public void recalculateEffectiveLength(){
        merit.setEffectiveLength(vm.getEffectiveLength());
    }
    
    public int getMerit() {
        return merit.getMeritValue();
    }
    
    public void setNeighbour(DigitalOrganism digorg) { neighbour = digorg; }
    public DigitalOrganism getNeighbour() { return neighbour; }
    
    public void setPositionInfo(Object o){
        position_info = o;
    }
    public Object getPositinInfo() { return position_info; }
    
    public String toString(){
        StringBuffer sb = new StringBuffer();
	sb.append("#Age: " + age + "\n");
	sb.append("#Fitness: " + getFitness() + "\n");
	sb.append("#Merit: " + merit.getMeritValue() + "\n");
	sb.append("#Effective length: " + vm.getEffectiveLength() + "\n");
	if (!(vm.getGestationTime() == vm.GESTATION_TIME_INVALID)){
	    sb.append("#Gestationtime: " + vm.getGestationTime() + "\n");
	}
	else{
	    sb.append("#Gestationtime: not known at this moment.");
	}
	sb.append("\n");
	
	//tasks
	sb.append(performed_tasks.toString());
	sb.append("\n" + getGenome());
	return sb.toString();
    }
    
}















