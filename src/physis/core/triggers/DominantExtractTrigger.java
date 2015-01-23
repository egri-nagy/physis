/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: DominantExtractTrigger.java,v 1.3 2003/05/08 12:49:56 sirna Exp $
 * $Revision: 1.3 $
 * $Date: 2003/05/08 12:49:56 $
 */
package physis.core.triggers;

import physis.core.PHYSIS;
import physis.core.DigitalOrganism;
import physis.core.iterator.DigitalOrganismIterator;
import physis.core.Configuration;
import physis.core.genotype.Genome;
import physis.log.Log;
import physis.util.HandyMethods;

import java.io.*;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class DominantExtractTrigger extends DataExtractTrigger{
    private String dir;
    
    DominantExtractTrigger(){
        population = PHYSIS.getInstance().getPopulation();
    }
    
    public void setParameters(StringTokenizer params){
	dir = Configuration.getPhysisHome() + System.getProperties().getProperty("file.separator") + params.nextToken();
    }
    
    public void execute(){
	try{
	    int max = 0;
	    MutableInteger act = null;
	    Genome g = null;
	    DigitalOrganism dom = null, org = null;
	    DigitalOrganismIterator it = population.getAllOrganism();
	    Hashtable genotypes = new Hashtable(population.getNumberOfLivingOrganisms());
	    while (it.hasNext()){
		org = it.next();
		if (org.isAlive()){
		    g = org.getGenome();
		    if (genotypes.containsKey(g)){
			act = ((MutableInteger)genotypes.get(g));
			act.i++;
			if (act.i > max){
			    max = act.i;
			    dom = org;
			}
		    }
		    else{
		        genotypes.put(g, new MutableInteger());
		    }
		}
	    }
	    FileWriter fw = new FileWriter(dir + System.getProperties().getProperty("file.separator")
					   + "dom_" + HandyMethods.getLongNumberString(PHYSIS.getInstance().getUpdateCount(),10)
					   + "_" + dom.getGenome().hashCode() );
	    fw.write("# Frequency: " + ((MutableInteger)genotypes.get(dom.getGenome())).i + "\n");
	    fw.write(dom.toString());
	    fw.close();
	} catch(IOException ioe){
	    Log.error(ioe);
	}
	if (is_periodical){
	    adjustPeriodicalSchedule();
	}
    }
    
    public String toString(){
        return "Extract dominant organism to directory " + dir + ".";
    }
    
    class MutableInteger{
        int i;
    }
}
