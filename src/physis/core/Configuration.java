/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Configuration.java,v 1.13 2003/05/16 13:39:50 sirna Exp $
 * $Revision: 1.13 $
 * $Date: 2003/05/16 13:39:50 $
 */
package physis.core;

import physis.log.*;

import java.util.*;
import java.io.*;

/**
 * This class contains the required parameters for the running system.
 * There are two types of parameters:<br>
 *<ol>
 *<li>
 *<b>Basic parameter&nbsp; - </b>It's often used and important for the basic
 * functionality of the system. It can be accessed quickly through a simple
 * method - named according to the parametername - in its type. It can be
 * accessed as property String as well.</li>
 * <li>
 * <b>Additional parameter </b>- It can be accessed only as property String.</li>
 * </ol>
 *<br>
 * CRC
 * <br>
 * 1. Retrieving and storing the configuration data.
 * <br>
 * 2. Ensure easy acces to the system configuration parameters.
 * <br>
 * 3. Changing parameters during the run.
 */
public class Configuration {
    //keys for basic properties
    public static String MAX_NUMBER_OF_UPDATES = "max_number_of_updates";
    public static String AVERAGE_TIME_SLICE = "average_time_slice";
    public static String MIN_ALLOCATION_RATIO = "min_allocation_ratio";
    public static String MAX_ALLOCATION_RATIO = "max_allocation_ratio";
    public static String MIN_PROLIFERATION_RATIO = "min_proliferation_ratio";
    public static String VM_CLASS_NAME = "virtual_machine_class_name";
    public static String NURSE_CLASS_NAME = "nurse_class_name";
    public static String GENE_BANK_ENABLED = "gene_bank_enabled";
    public static String GENE_BANK_MAXSIZE = "gene_bank_max_size";
    public static String MAX_NUMBER_OF_TASKS = "max_number_of_tasks";
    public static String PHYSIS_HOME = "physis_home";

    static Properties props = new Properties();

    public static void init(String config_file) {
        Log.status("LOADED PARAMETERS: ");
        try{
            props.load(new FileInputStream(config_file));
        } catch (IOException ioe) {
            Log.error("Error while reading configuration file: " + config_file + ", " + ioe.getMessage());
            Log.error("No parameters, no digital evolution. Such is life...");
            System.exit(-1);
        }
        //if the system is alive after config_file loading then we can continue, setting basic parameters.. dummy work
        setMaxNumberOfUpdates(Long.parseLong(props.getProperty(MAX_NUMBER_OF_UPDATES)));
        setAverageTimeSlice(Integer.parseInt(props.getProperty(AVERAGE_TIME_SLICE)));
        setMinAllocationRatio(Double.parseDouble(props.getProperty(MIN_ALLOCATION_RATIO)));
        setMaxAllocationRatio(Double.parseDouble(props.getProperty(MAX_ALLOCATION_RATIO)));
        setMinProliferationRatio(Double.parseDouble(props.getProperty(MIN_PROLIFERATION_RATIO)));
        setVMClassName(props.getProperty(VM_CLASS_NAME));
        setGeneBankEnabled("true".equalsIgnoreCase(props.getProperty(GENE_BANK_ENABLED)));
        setGeneBankMaxSize(Integer.parseInt(props.getProperty(GENE_BANK_MAXSIZE)));
        if (props.getProperty(MAX_NUMBER_OF_TASKS) != null){
            setMaxNumberOfTasks(Integer.parseInt(props.getProperty(MAX_NUMBER_OF_TASKS)));
        }
        setPhysisHome(props.getProperty(PHYSIS_HOME));
    }

    /**
     * Provides acces for non-frequently used parameters.
     */
    public static String getProperty(String paramname) { return props.getProperty(paramname); }

    /**
     * Examines whether the given parameter exists or not.
     */
    public static boolean doesExistProperty(String paramname){
        return props.getProperty(paramname) != null;
    }

    /** The maximum number of updates that the system is allowed to perform. */
    private static long maximum_number_of_updates;
    public static void setMaxNumberOfUpdates(long max_updates){
        maximum_number_of_updates = max_updates;
        Log.status("Maximum number of updates = " + maximum_number_of_updates);
    }
    public static long getMaxNumberOfUpdates() { return maximum_number_of_updates; }

    /** Average number of performed VM cycles per update. */
    private static int average_time_slice;
    public static void setAverageTimeSlice(int avg_times_lice) {
        average_time_slice = avg_times_lice;
        Log.status("Average time slice = " + avg_times_lice);
    }
    public static int getAverageTimeSlice() { return average_time_slice; }

    /** When a digital organism tries to allocate space for its child it's only allowed
     * to do it if the size of the allocated area is bigger than original_size * min_allocation_ratio.
     */
    private static double min_allocation_ratio;
    public static void setMinAllocationRatio(double min_alloc_ratio) {
        min_allocation_ratio = min_alloc_ratio;
        Log.status("Minimum allocation ratio = " + min_allocation_ratio);
    }
    public static double getMinAllocationRatio() { return min_allocation_ratio; }

    /** When a digital organism tries to allocate space for its child it's only allowed
     * to do it if the size of the allocated area is smaller than original_size * max_allocation_ratio.
     */
    private static double max_allocation_ratio;
    public static void setMaxAllocationRatio(double max_alloc_ratio) {
        max_allocation_ratio = max_alloc_ratio;
        Log.status("Maximum allocation ratio = " + max_allocation_ratio);
    }
    public static double getMaxAllocationRatio() { return max_allocation_ratio; }


    private static double min_proliferation_ratio;
    public static void setMinProliferationRatio(double min_proli_ratio) {
        min_proliferation_ratio = min_proli_ratio;
        Log.status("Minimum proliferation ratio = " + min_proliferation_ratio);
    }
    public static double getMinProliferationRatio() { return min_proliferation_ratio; }

    /**
     * It comes from when building the tasklibrary.
     */
    private static int max_task_size;
    public static int getMaxTaskSize() { return max_task_size; }
    public static void setMaxTaskSize(int max_task) {
        max_task_size = max_task;
        Log.status("Maximum task size set to " + max_task_size);
    }

    /**
     * If it's less than the actual loaded tasks then it may be reset by TaskLibrary.
     */
    private static int max_number_of_tasks;
    public static int getMaxNumberOfTasks() { return max_number_of_tasks; }
    public static void setMaxNumberOfTasks(int max_num_of_tasks) {
        max_number_of_tasks = max_num_of_tasks;
        Log.status("Maximum number of tasks set to " + max_number_of_tasks);
    }



    private static String vm_class_name;
    private static void setVMClassName(String vm_name){
        vm_class_name = vm_name;
        Log.status("VM class name = " + vm_class_name);
    }
    public static String getVMClassName() { return vm_class_name; }
    public static String getNurseClassName() { return props.getProperty(NURSE_CLASS_NAME); }

    private static boolean geneBankEnabled;
    public static void setGeneBankEnabled(boolean enabled){
        geneBankEnabled = enabled;
        if (geneBankEnabled){
            Log.status("Genebank enabled.");
        }
        else{
            Log.status("Genebank disabled");
        }
    }
    public static boolean isGeneBankEnabled() { return geneBankEnabled; }

    private static int genebank_max_size;
    public static void setGeneBankMaxSize(int size) {genebank_max_size = size; }
    public static int getGeneBankMaxSize() { return genebank_max_size; }

    private static String physis_home;
    public static void setPhysisHome(String home) {
        if ((home.charAt(home.length() - 1) == '/') || (home.charAt(home.length() - 1) == '\\'))
            physis_home = home;
        else
            physis_home = home + '/';
    }
    public static String getPhysisHome() { return physis_home; }
    /** Returns the version number and codename. */
    public static String getVersion() { return "Physis 0.7 Codename:ECAL2003";}
}
