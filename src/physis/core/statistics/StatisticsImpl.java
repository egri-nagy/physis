/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: StatisticsImpl.java,v 1.4 2002/03/26 09:59:27 sirna Exp $
 * $Revision: 1.4 $
 * $Date: 2002/03/26 09:59:27 $
 */
package physis.core.statistics;

import physis.core.PHYSIS;
import physis.core.Configuration;
import physis.core.Population;
import physis.log.Log;

import java.io.*;
import java.util.Properties;

/**
 * Writes statistical information to a file. This is for easily calculatable information (like max and average values).
 * For getting more computational intensive information thriggers should be used<BR>
 * It needs two parameters: <BR>
 * statistics_configuration - the filename (relative path from physis_home) of the configurationfile containing the settings for statistical data gathering <BR>
 * statistics_data_file - the filename into which the statistical dat should be written
 */
public class StatisticsImpl implements Statistics{
    protected final String STATISTICS_CONFIGURATION = "statistics_configuration";
    protected final String STATISTICS_DATA_FILE = "statistics_data_file";
    protected final String SAMPLE_RATE = "sample_rate";

    //the order should remain in every methods in this class
    protected String nf_s = "minimum_fitness";
    protected boolean nf;
    protected String xf_s = "maximum_fitness";
    protected boolean xf;
    protected String xfer_s = "maximum_fitness_ever_reached";
    protected boolean xfer;
    protected String avgf_s = "average_fitness";
    protected boolean avgf;
    protected String nm_s = "minimum_merit";
    protected boolean nm;
    protected String xm_s = "maximum_merit";
    protected boolean xm;
    protected String xmer_s = "maximum_merit_ever_reached";
    protected boolean xmer;
    protected String avgm_s = "average_merit";
    protected boolean avgm;
    protected String xa_s = "maximum_age";
    protected boolean xa;
    protected String xaer_s = "maximum_age_ever_reached";
    protected boolean xaer;
    protected String avga_s = "average_age";
    protected boolean avga;
    protected String nfo_s = "number_of_fertile_organisms";
    protected boolean nfo;
    protected String nlo_s = "number_of_living_organisms";
    protected boolean nlo;
    protected String xgl_s = "maximum_genome_length";
    protected boolean xgl;
    protected String xgler_s = "maximum_genome_length_ever_reached";
    protected boolean xgler;
    protected String avggl_s = "average_genome_length";
    protected boolean avggl;
    protected String xel_s = "maximum_effective_length";
    protected boolean xel;
    protected String xeler_s = "maximum_effective_length_ever_reached";
    protected boolean xeler;
    protected String avgel_s = "average_effective_length";
    protected boolean avgel;


    protected BufferedWriter bw;
    protected Population pop;

    protected int sample_rate;

    public StatisticsImpl(Population pop_){
        String stat_conf = Configuration.getPhysisHome() + Configuration.getProperty(STATISTICS_CONFIGURATION);
	String data_file = Configuration.getPhysisHome() + Configuration.getProperty(STATISTICS_DATA_FILE);
	try{
	    Properties conf = new Properties();
	    conf.load(new FileInputStream(stat_conf));
	    processSettings(conf);
	    bw = new BufferedWriter(new FileWriter(data_file));
	    printHeader();
	    pop = pop_;
	} catch (IOException ioe){
	    Log.error("Couldn't instantiate statistics" + ioe.getMessage());
	}
    }

    protected void processSettings(Properties conf){
	sample_rate = Integer.parseInt(conf.getProperty(SAMPLE_RATE));
        if ("yes".equals(conf.getProperty(nf_s))) nf = true;
        if ("yes".equals(conf.getProperty(xf_s))) xf = true;
        if ("yes".equals(conf.getProperty(xfer_s))) xfer = true;
        if ("yes".equals(conf.getProperty(avgf_s))) avgf = true;
        if ("yes".equals(conf.getProperty(nm_s))) nm = true;
        if ("yes".equals(conf.getProperty(xm_s))) xm = true;
        if ("yes".equals(conf.getProperty(xmer_s))) xmer = true;
        if ("yes".equals(conf.getProperty(avgm_s))) avgm = true;
        if ("yes".equals(conf.getProperty(xa_s))) xa = true;
        if ("yes".equals(conf.getProperty(xaer_s))) xaer = true;
        if ("yes".equals(conf.getProperty(avga_s))) avga = true;
        if ("yes".equals(conf.getProperty(nfo_s))) nfo = true;
        if ("yes".equals(conf.getProperty(nlo_s))) nlo = true;
        if ("yes".equals(conf.getProperty(xgl_s))) xgl = true;
        if ("yes".equals(conf.getProperty(xgler_s))) xgler = true;
        if ("yes".equals(conf.getProperty(avggl_s))) avggl = true;
        if ("yes".equals(conf.getProperty(xel_s))) xel = true;
        if ("yes".equals(conf.getProperty(xeler_s))) xeler = true;
        if ("yes".equals(conf.getProperty(avgel_s))) avgel = true;
    }

    public void gatherInformation(long update){
	if ((update % sample_rate) == 0){
	    try{
		bw.write(update + "\t");
		if (nf) bw.write(pop.getMinFitness() + "\t");
		if (xf) bw.write(pop.getMaxFitness() + "\t");
		if (xfer) bw.write(pop.getMaxFitnessEverReached() + "\t");
		if (avgf) bw.write(pop.getAverageFitness() + "\t");
		if (nm) bw.write(pop.getMinMerit() + "\t");
		if (xm) bw.write(pop.getMaxMerit() + "\t");
		if (xmer) bw.write(pop.getMaxMeritEverReached() + "\t");
		if (avgm) bw.write(pop.getAverageMerit() + "\t");
		if (xa) bw.write(pop.getMaxAge() + "\t");
		if (xaer) bw.write(pop.getMaxAgeEverReached() + "\t");
		if (avga) bw.write(pop.getAverageAge() + "\t");
		if (nfo) bw.write(pop.getNumberOfFertileOrganisms() + "\t");
		if (nlo) bw.write(pop.getNumberOfLivingOrganisms() + "\t");
		if (xgl) bw.write(pop.getMaxGenomeLength() + "\t");
		if (xgler) bw.write(pop.getMaxGenomeLengthEverReached() + "\t");
		if (avggl) bw.write(pop.getAverageGenomeLength() + "\t");
		if (xel) bw.write(pop.getMaxEffectiveLength() + "\t");
		if (xeler) bw.write(pop.getMaxEffectiveLengthEverReached() + "\t");
		if (avgel) bw.write(pop.getAverageEffectiveLength() + "\t");
		bw.write("\n");
		bw.flush();
	    }catch (IOException ioe){
	        Log.error("Error while writing the statistical data." + ioe.getMessage());
	    }
	}
    }

    protected void printHeader() throws IOException{
	bw.write("update" + "\t");
	if (nf) bw.write(nf_s + "\t");
	if (xf) bw.write(xf_s + "\t");
	if (xfer) bw.write(xfer_s + "\t");
	if (avgf) bw.write(avgf_s + "\t");
	if (nm) bw.write(nm_s + "\t");
	if (xm) bw.write(xm_s + "\t");
	if (xmer) bw.write(xmer_s + "\t");
	if (avgm) bw.write(avgm_s + "\t");
	if (xa) bw.write(xa_s + "\t");
	if (xaer) bw.write(xaer_s + "\t");
	if (avga) bw.write(avga_s + "\t");
	if (nfo) bw.write(nfo_s + "\t");
	if (nlo) bw.write(nlo_s + "\t");
	if (xgl) bw.write(xgl_s + "\t");
	if (xgler) bw.write(xgler_s + "\t");
	if (avggl) bw.write(avggl_s + "\t");
	if (xel) bw.write(xel_s + "\t");
	if (xeler) bw.write(xeler_s + "\t");
	if (avgel) bw.write(avgel_s + "\t");
	bw.write("\n");
	bw.flush();
    }


}
