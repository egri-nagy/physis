/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: PhysisVMTracer.java,v 1.12 2003/05/16 13:03:14 sirna Exp $
 * $Revision: 1.12 $
 * $Date: 2003/05/16 13:03:14 $
 */
package physis.core.virtualmachine;

import physis.core.*;
import physis.core.task.TaskLibrary;
import physis.core.lifespace.LifeSpace;
import physis.core.random.Randomness;
import physis.core.random.RandomnessFactory;
import physis.core.event.*;

import java.util.*;
/**
 * A separate tool for running one virtualmachine. The execution can be monitored by the state dumps
 * produced by the virtual machine itself.
 */
public final class PhysisVMTracer implements Environment{
    private static String TASK_FILE = "task_filename";
    private static String INPUT_DATA_HIGHER_BOUND = "input_data_higher_bound";
    
    //states
    private static final int BEGIN = 0;
    private static final int REPLICATED_ONCE = 1;
    
    /** The number of executed instructions which can be considered as inifinite loop. */
    private static final int INFINITE_LOOP = 10000;
    
    private TaskLibrary tasklibrary;
    private Randomness rnd = RandomnessFactory.createRandomness();
    private DigitalOrganism org;
    private PhysisVirtualMachine vm;
    private GeneticCodeTape orig;
    private int input_data_higher_bound;
    private boolean ready = false;
    private int state;

    //what information to write
    private boolean dump_io = false;
    private static String S_DUMP_IO = "io";
    private boolean dump_state = false;
    private static String S_DUMP_STATE = "state";
    private boolean dump_cpu = false;
    private static String S_DUMP_CPU = "cpu";
    
    public static void main(String[] args){
        //processing the arguments
	String codetape_file = null;
	String task_file = null;
	if (args.length < 2){
	    usage();
	    System.exit(1);
	}
	Configuration.init(args[0]);
	PhysisVMTracer tracer = new PhysisVMTracer();
	codetape_file = args[1];
	if (args.length >= 3){
	    for (int i=2; i < args.length; i++){
		if (S_DUMP_IO.equals(args[i])){ tracer.dump_io = true; }
		if (S_DUMP_STATE.equals(args[i])){ tracer.dump_state = true; }
		if (S_DUMP_CPU.equals(args[i])){ tracer.dump_cpu = true; }
	    }
	}
	
	PHYSIS.environment = tracer;
	tracer.input_data_higher_bound = Integer.parseInt(Configuration.getProperty(INPUT_DATA_HIGHER_BOUND));
	InstructionSet.getInstance();
	tracer.tasklibrary = new TaskLibrary();
	tracer.tasklibrary.buildLibrary(Configuration.getPhysisHome() + Configuration.getProperty(TASK_FILE));
	tracer.org = new DigitalOrganismImpl();
        //hacking the environment reference
	tracer.vm = tracer.org.getVM();
	GeneticCodeTapeFactory.init();
	tracer.orig = GeneticCodeTapeFactory.getGeneticCodeTape(codetape_file);
        tracer.org.vivify(new DigitalOrganismImpl(),tracer.orig,0);
	if (tracer.dump_cpu) { System.out.println("Processor Information:\n" + tracer.vm.getProcessorInformation());}
	if (tracer.dump_state){ System.out.println("INITIAL STATE: " + tracer.vm.getState());}
	int counter = 0;
	while (counter < INFINITE_LOOP || tracer.ready){
	    tracer.org.update();
	    if (tracer.dump_state){
		System.out.println(tracer.vm.getState());
	    }
	    counter++;
	}
	System.out.println("Genetic code haven't replicated after executing " + INFINITE_LOOP + " instructions.");
    }
    
    private static void usage(){
        System.out.println("Usage: java physis.core.virtualmachine.PhysisVMTracer <properties> <initial codetape file>  [cpu] [state] [io]");
        System.out.println("cpu - processor structure & instructionset,  state - states of the machine, io - dumping I/O actvity and performed tasks");
        System.out.println("Example: java physis.core.virtualmachine.PhysisVMTracer data/physis.props data/genebank/mar/mar.short");
    }
    
    /** Returns the tasklibrary. */
    public TaskLibrary getTaskLibrary(){ return tasklibrary; }
    
    
    public void setLifeSpace(LifeSpace lifespace_) {}
    public LifeSpace getLifeSpace() { return null; }
    
    //as InteractionListener
    public void interactionOccured(InteractionEvent ie){
        try{
	    DigitalOrganism actor = ie.getOrganism();
	    if (dump_io) {System.out.println(actor.getMetabolism());}
	    actor.recalculateBonus(tasklibrary.checkIOActivity(actor.getMetabolism(), actor.getPerformedTasks()));
	    if (dump_io) {System.out.println(actor.getPerformedTasks());}
        } catch(Exception e){
	    e.printStackTrace();
        }
	
    }
    
    //as ProliferationListener
    public void proliferationPerformed(ProliferationEvent pe){
	switch(state){
	    case BEGIN:{
		    System.out.println("FIRST CELL-DIVISION");
		    processProliferationEvent(pe);
		    state = REPLICATED_ONCE;
		} break;
	    case REPLICATED_ONCE:{
		    System.out.println("SECOND CELL-DIVISION");
		    processProliferationEvent(pe);
		    System.exit(0);
		} break;
		
	}//switch
    }
    
    private void processProliferationEvent(ProliferationEvent pe){
	DigitalOrganism parent = pe.getOrganism();
	parent.recalculateEffectiveLength();
	System.out.println("Gestation time: " + parent.getVM().getGestationTime());
	System.out.println("Genomelength: " + parent.getGenomeSize());
	System.out.println("Effective length: " + parent.getEffectiveLength());
	parent.getVM().restart();
	GeneticCodeTape child = pe.getNewSeed();
	System.out.println(child.getGenome());
	if (orig.getGenome().equals(child.getGenome())){
	    System.out.println("Child is identical.\n");
	}
	else{
	    System.out.println("Corrupted copy.\n");
	}
    }
    
    /**
     * This provides the input from the environment (~food).
     */
    public int getInputData() {
	return rnd.nextInt(input_data_higher_bound); }
    
    /**
     * Rotates the organism's facing forward.
     */
    public void rotateForward(DigitalOrganism digorg){
    }
    
    /**
     * Answers the question: Should the current copied instruction be mutated?
     */
    public boolean copyShouldBeMutated(){
	return false;
    }
    
    /**
     * Answers the question: Should the current divided codetape be mutated?
     */
    public boolean divideShouldBeMutated(){
	return false;
    }
    
    /**
     * Answers the question: Should the current divided codetape be inserted extra instructions?
     */
    public boolean shouldBeInserted(){
	return false;
    }
    
    /**
     * Answers the question: Should the current divided codetape be deleted one instruction?
     */
    public boolean shouldBeDeleted(){
	return false;
    }
    
    
    public DigitalOrganism getNeighbourRandomly(DigitalOrganism digorg){
	return null;
    }
    
    public Randomness getRandomness() { return rnd; }
    
}
