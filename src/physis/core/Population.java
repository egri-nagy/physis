/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Population.java,v 1.29 2003/05/15 16:42:50 sirna Exp $
 * $Revision: 1.29 $
 * $Date: 2003/05/15 16:42:50 $
 */
package physis.core;

import physis.core.scheduler.Scheduler;
import physis.core.scheduler.SchedulerFactory;
import physis.core.virtualmachine.GeneticCodeTapeFactory;
import physis.core.iterator.DigitalOrganismIterator;
import physis.core.lifespace.LifeSpace;
import physis.core.lifespace.LifeSpaceFactory;

import physis.log.Log;

/**
 * Population class represents the whole population running in one concrete instance of the system. When the system is distributed through the network it represents all the digital organisms living on the same node.
 * <br>
 * CRC
 * <br>
 * 1. does the updates of organism - Scheduler
 * <br>
 * 2. after each update it refreshes the state of the population described by state variables.
 */
public class Population {

    private static final String SEED_ORGANISM = "seed_organism";

    private Scheduler scheduler;
    private LifeSpace lifespace;

    //General parameters describing the state of the whole population.
    private int max_age;
    private int max_age_ever_reached;
    private double average_age;
    private int max_merit;
    private int max_merit_ever_reached;
    private int min_merit;
    private double average_merit;
    private double max_fitness;
    private double max_fitness_ever_reached;
    private double min_fitness;
    private double average_fitness;
    private int max_genome_length;
    private int max_genome_length_ever_reached;
    private double average_genome_length;
    private int max_effective_length;
    private int max_effective_length_ever_reached;
    private double average_effective_length;
    private int number_of_living_organisms;
    private int number_of_fertile_organisms;

    //references to special orgs
    private DigitalOrganism max_fitness_org;
    private DigitalOrganism max_merit_org;
    private DigitalOrganism max_age_org;
    private DigitalOrganism max_glength_org;
    private DigitalOrganism max_elength_org;

    public Population(){
        scheduler = SchedulerFactory.getScheduler();
        lifespace = LifeSpaceFactory.getInstance();

        //inserting the seed organisms
        int seedorg_counter = 1;
        while (true){
	    String seedorg_name =  Configuration.getProperty(SEED_ORGANISM + seedorg_counter);
	    if (seedorg_name != null){
		lifespace.injectGenome(GeneticCodeTapeFactory.getGeneticCodeTape(Configuration.getPhysisHome() + seedorg_name));
		seedorg_counter++;
	    }
	    else{
		break;
	    }
        }

        PHYSIS.environment.setLifeSpace(lifespace);
        scheduler.fillWithOrganisms(lifespace.getAllOrganisms());
    }

    /** Updating the whole population by executing a given average number of instructions in each CPU. */
    public void update() {
        // doing the update via the scheduling algorithm
        scheduler.scheduleUpdate(Configuration.getAverageTimeSlice());
        // increasing the age of the living orgnisms
        updateState();
    }

    /**
     *  This method updates the data which represents the population state.
     */
    private void updateState(){
        try{
	    //initializing 'global' variables
	    max_merit = 0;
	    min_merit = Integer.MAX_VALUE;
	    max_fitness = 0.0;
	    min_fitness = Double.POSITIVE_INFINITY;
	    max_age = 0;
	    max_genome_length = 0;
	    max_effective_length = 0;
	    average_effective_length = average_genome_length = average_fitness = average_age = average_merit = 0.0;

	    //getting the organism iterator
	    DigitalOrganismIterator iterator = lifespace.getAllOrganisms();

	    int merit = 0;
	    double fitness = 0;
	    int living_orgs = 0;
	    int fertile_orgs = 0;
	    long merit_sum = 0;
	    double fitness_sum = 0;
	    int genome_length = 0;
	    long glength_sum = 0;
	    int effective_length = 0;
	    long elength_sum = 0;
	    long age_sum =0;
	    int age = 0;

	    while (iterator.hasNext()){
		DigitalOrganism digorg = iterator.next();
		// if the organism is alive then we should check the parameters
		if (digorg.isAlive()){
		    //this is needed because we want to know if the orgainm newborn or not
		    age = digorg.getAge();

		    //first updating the organims itself if it's alive
		    //the newborn's should use the inherited value in their first update
		    if (age != 0) digorg.recalculateEffectiveLength();
		    digorg.increaseAge();
		    age++;//otherwise some values would be more than max
		    living_orgs++;

		    merit = digorg.getMerit();

		    //merit
		    if (merit < min_merit) {
			min_merit = merit;
		    }
		    if (merit > max_merit){
			max_merit = merit;
			max_merit_org = digorg;
		    }
		    merit_sum += merit;

		    //we are interested in some parameters only if the organism is fertile
		    if (digorg.isFertile()) {
			fertile_orgs++;
			fitness = digorg.getFitness();
			genome_length = digorg.getGenome().size();
			effective_length = digorg.getEffectiveLength();
			//sums
			age_sum += age;
			fitness_sum += fitness;
			glength_sum += genome_length;
			elength_sum += effective_length;

			//age
			if (max_age < age){
			    max_age = age;
			    max_age_org = digorg;
			}


			//fitness
			if (fitness < min_fitness) {
			    min_fitness = fitness;
			}
			if (fitness > max_fitness){
			    max_fitness = fitness;
			    max_fitness_org = digorg;
			}

			//genome_length
			if (max_genome_length < genome_length){
			    max_genome_length = genome_length;
			    max_glength_org = digorg;
			}

			//effective_length
			if (max_effective_length < effective_length){
			    max_effective_length = effective_length;
			    max_elength_org = digorg;
			}


		    }

		}
	    }
	    number_of_living_organisms = living_orgs;
	    number_of_fertile_organisms = fertile_orgs;

	    if (max_age_ever_reached < max_age) { max_age_ever_reached = max_age; }
	    if (max_fitness_ever_reached < max_fitness) { max_fitness_ever_reached = max_fitness; }
	    if (max_merit_ever_reached < max_merit) { max_merit_ever_reached = max_merit; }
	    if (max_effective_length_ever_reached < max_effective_length) { max_effective_length_ever_reached = max_effective_length;}
	    if (max_genome_length_ever_reached < max_genome_length) { max_genome_length_ever_reached = max_genome_length;}


	    if (number_of_living_organisms != 0){
		average_merit = (double) merit_sum / number_of_living_organisms;
	    }

	    if (number_of_fertile_organisms != 0){
		average_fitness = fitness_sum / fertile_orgs;
		average_genome_length = (double) glength_sum / fertile_orgs;
		average_effective_length = (double) elength_sum /fertile_orgs;
		average_age = (double) age_sum / fertile_orgs;
	    }
        } catch(Exception e){
	    Log.error(e);
        }

    }

    public DigitalOrganismIterator getAllOrganism() { return lifespace.getAllOrganisms(); }

    public int getPopulationSize() {
        return lifespace.getSize();
    }

    /**
     * Gives the current number of living organism in the lifespace.
     */
    public int getNumberOfLivingOrganisms(){
        return  number_of_living_organisms;
    }


    /**
     * Gives the current number of fertile organism in the lifespace.
     */
    public int getNumberOfFertileOrganisms(){
        return number_of_fertile_organisms;
    }

    public int getMaxAge() { return max_age; }
    public int getMaxAgeEverReached() { return max_age_ever_reached; }
    public double getAverageAge() { return average_age; }
    public int getMaxMerit() { return max_merit; }
    public int getMaxMeritEverReached() { return max_merit_ever_reached; }
    public int getMinMerit() { return min_merit; }
    public double getAverageMerit() { return average_merit; }
    public double getMaxFitness() { return max_fitness; }
    public double getMaxFitnessEverReached() { return max_fitness_ever_reached; }
    public double getMinFitness() { return min_fitness; }
    public double getAverageFitness() { return average_fitness; }
    public int getMaxGenomeLength() { return max_genome_length; }
    public int getMaxGenomeLengthEverReached() { return max_genome_length_ever_reached; }
    public double getAverageGenomeLength() { return average_genome_length; }
    public int getMaxEffectiveLength() { return max_effective_length; }
    public int getMaxEffectiveLengthEverReached() { return max_effective_length_ever_reached; }
    public double getAverageEffectiveLength() { return average_effective_length; }

    public DigitalOrganism getMaxFitnessOrganism() { return max_fitness_org ;}
    public DigitalOrganism getMaxMeritOrganism() { return max_merit_org ;}
    public DigitalOrganism getMaxGenomeLengthOrganism() { return max_glength_org ;}
    public DigitalOrganism getMaxEffectiveLengthOrganism() { return max_elength_org ;}
    public DigitalOrganism getMaxAgeOrganism() { return max_age_org ;}

    public String toString(){
        return "AGE max: " + max_age + " maxer: " + max_age_ever_reached + " avg: " + average_age
	    + "\nFITNESS max:" + max_fitness +  " maxer: " + max_fitness_ever_reached + " min: " + min_fitness + " avg: " + average_fitness
	    + "\nMERIT max: " + max_merit + " maxer: " + max_merit_ever_reached + " min: " + min_merit + " avg: " + average_merit
	    + "\nEFFECTIVE LENGTH max: " + max_effective_length + " maxer: " + max_effective_length_ever_reached + " avg: " + average_effective_length
	    + "\nGENOME LENGTH max: " + max_genome_length + " maxer: " + max_genome_length_ever_reached + " avg: " + average_genome_length
	    + "\n";
    }

}







