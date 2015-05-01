/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: PHYSIS.java,v 1.22 2000/10/30 17:09:00 sirna Exp $
 * $Revision: 1.22 $
 * $Date: 2000/10/30 17:09:00 $
 */
package physis.core;

import physis.core.statistics.Statistics;
import physis.core.statistics.StatisticsFactory;
import physis.core.triggers.Triggers;
import physis.core.virtualmachine.GeneticCodeTapeFactory;
import physis.core.virtualmachine.InstructionSet;
import physis.log.Log;
import physis.visualisation.Viewer;

import java.io.File;

/**
 * This is the main starter class. It has the main method as the entry point of the system.<BR>
 * Other alternative to run the system is running this class as a thread. It allows full controll over the processing.
 * The name of the config file should be set very first!
 * (the main method starts a thread as well)
 * <BR>
 * CRC
 * <BR>
 * 1. instantiating the system - Configuration, Population, statistics, visualisation
 * <BR>
 * 2. do the updates - Population, statistics, visualisation, viewers
 *
 * @stereotype main
 */
public final class PHYSIS extends Thread{
    /**
     * Mainly for performance reasons this reference can be publicly available this way: PHYSIS.environment <br>
     * The default environment is EnvironmentImpl is instantiated by PHYSIS.
     */
    public static Environment environment;

    /** The name of the file containing the configuration parameters (with full path). */
    private static String config_file = "";
    /** The instance of the system. */
    private static PHYSIS physis;
    /** There's one population in one instance of Physis. */
    private Population population;
    /** The container of predefined events, operations. */
    private Triggers triggers;
    /** The statistics-generator subsystem. */
    private Statistics statistics;
    /** Several different viewers can be attached to the system. The active viewers are stored in this array. */
    private Viewer[] viewers = new Viewer[0];
    /** Counts the number of updates. Starting from 1. */
    private long updatecount = 1;
    /** The maximum number of possible updates is defined in the configuration file. */
    private long max_updates;

    /**
     * This is the main entry point.
     */
    public static void main(String[] args) {
        //sanity check - the only argument we need the (full) name of the configuration file.
        if ( (args.length != 1) || (!(new File(args[0]).isFile())) ){
            System.out.println("USAGE: java physis.core.PHYSIS configuration_file");
            System.exit(-1);
        }
        Log.status(Log.getFullTimeStamp());
        PHYSIS.setConfigFileName(args[0]);
        PHYSIS physis = PHYSIS.getInstance();
        physis.start();
    }

    /**
     * Sets the configuration file. After this the system can be instantiated.
     */
    public static void setConfigFileName(String filename){
        config_file = filename;
        Log.status("CONFIGURATION FILE: " + config_file);
    }

    /** This returns an instance of the system if the full path of the configuration file is correctly set. */
    public static PHYSIS getInstance(){
        if (physis != null){
        }else{
            if ("".equals(config_file)){
                Log.error("Filename of the configfile isn't set. Unable to start the core system.");
                System.exit(1);
            }
            else{
                physis = new PHYSIS(config_file);
            }
        }
        return physis;
    }

    /** The thread's run method. It's a cycle in which the system parts are updated. */
    @Override
    public void run(){
        //it should be set to minimum priority in order to get the GUI work properly
        setPriority(Thread.MIN_PRIORITY);

        triggers = new Triggers();

        //just some message saying everything is ok
        Log.printMemoryInfo();
        Log.status("PHYSIS system is ready to run " + max_updates + " updates.\n");
        try{
            while(updatecount < max_updates){

                //updating the state of the population
                population.update();

                //checking for triggers to be executed
                physis.triggers.processActualTriggers(physis.updatecount);

                //gathering statistical  information
                physis.statistics.gatherInformation(updatecount);

                //refreshing the viewers
                for (Viewer viewer : viewers) {viewer.refresh();}

                //Log.status(physis.updatecount + ". update finished.");
                //Log.printMemoryInfo();
                updatecount++;


            }
        }catch (Exception e){
            Log.printMemoryInfo();
            Log.error(e);
        }
        Log.status("THE END");

    }

    private PHYSIS(String config_file){
        Log.status("PHYSIS core system is starting...");

        //first loading the parameters
        Configuration.init(config_file);

        //instantiating the environment
        environment = new EnvironmentImpl();

        //instantiating InstructionSet
        InstructionSet.getInstance();

        GeneticCodeTapeFactory.init();

        //instantiating the system
        population = new Population();

        //statistics instantiating
        statistics = StatisticsFactory.getStatistics(population);

        //Setting the maximum number of updates
        max_updates = Configuration.getMaxNumberOfUpdates();
    }

    /** Returns the instance's population. */
    public Population getPopulation(){ return population; }

    /** Returns the number of the current update cycle. */
    public long getUpdateCount() { return updatecount; }

    /** The viewers can be set dynamically. */
    public void setViewers(Viewer[] viewers){
        this.viewers = viewers;
    }
}

