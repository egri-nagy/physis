/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: ProcessorStructureExtractTrigger.java,v 1.3 2003/05/15 16:46:44 sirna Exp $
 * $Revision: 1.3 $
 * $Date: 2003/05/15 16:46:44 $
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
import java.util.TreeMap;
import java.util.Iterator;
import java.util.StringTokenizer;

public class ProcessorStructureExtractTrigger extends DataExtractTrigger{
    private String dir;
    
    ProcessorStructureExtractTrigger(){
        population = PHYSIS.getInstance().getPopulation();
    }
    
    public void setParameters(StringTokenizer params){
	dir = Configuration.getPhysisHome() + System.getProperties().getProperty("file.separator") + params.nextToken();
    }
    
    public void execute(){
	try{
	    //TODO: sorting by elements, not keys...
	    MutableInteger act = null;
	    String  procinfo = null, archinfo = null, instsetinfo = null;
	    DigitalOrganism dom = null, org = null;
	    
	    DigitalOrganismIterator it = population.getAllOrganism();
	    TreeMap archs = new TreeMap();
	    int doma = 0, domi = 0;
	    String domarch = null, dominst = null;
	    TreeMap insts = new TreeMap();
	    while (it.hasNext()){
		org = it.next();
		if (org.isAlive()){
		    procinfo = org.getVM().getProcessorInformation();

		    BufferedReader br = new BufferedReader(new StringReader(procinfo));
		    String s = null;
		    while ( (s = br.readLine()) != null){
 			if (s.lastIndexOf('@') != -1){
			    archinfo = s;
			}
 			if (s.lastIndexOf('#') != -1){
			    instsetinfo = s;
			}

		    }
		    
		    //archinfo
		    if (archs.containsKey(archinfo)){
			act = ((MutableInteger)archs.get(archinfo));
			act.i++;
			if (act.i > doma){ domarch = archinfo; doma = act.i;} 
		    }
		    else{
		        archs.put(archinfo, new MutableInteger());
		    }
		    //instinfo
		    if (insts.containsKey(instsetinfo)){
			act = ((MutableInteger)insts.get(instsetinfo));
			act.i++;
			if (act.i > domi){ dominst = instsetinfo;domi = act.i;} 
		    }
		    else{
		        insts.put(instsetinfo, new MutableInteger());
		    }
		}
	    }

	    //writing archinfo	    
	    FileWriter fw = new FileWriter(dir + System.getProperties().getProperty("file.separator")
					   + "arch_distrib_" + HandyMethods.getLongNumberString(PHYSIS.getInstance().getUpdateCount(),10));
	    Iterator iter = archs.keySet().iterator();
	    fw.write("#DOMINANT: " + doma + " " + domarch + "\n");
	    while (iter.hasNext()){
		String m = (String) iter.next();
		fw.write(m + "frequency:" + ((MutableInteger)archs.get(m)).i +"\n");
	    }
	    fw.close();


	    //writing instinfo	    
	    fw = new FileWriter(dir + System.getProperties().getProperty("file.separator")
					   + "insts_distrib_" + HandyMethods.getLongNumberString(PHYSIS.getInstance().getUpdateCount(),10));
	    iter = insts.keySet().iterator();
	    fw.write("#DOMINANT: " + domi + " " +  dominst + "\n");
	    while (iter.hasNext()){
		String m = (String) iter.next();
		fw.write(m + "frequency:" + ((MutableInteger)insts.get(m)).i +"\n");
	    }
	    fw.close();

	} catch(IOException ioe){
	    Log.error(ioe);
	}
	if (is_periodical){
	    adjustPeriodicalSchedule();
	}
    }
    
    public String toString(){
        return "Extract processor structure distribution snapshots to directory " + dir + ".";
    }
    
    class MutableInteger{
        int i = 1;
    }
}
