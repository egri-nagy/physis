/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: GeneticSnapshot.java,v 1.2 2001/06/01 07:57:38 sirna Exp $
 * $Revision: 1.2 $
 * $Date: 2001/06/01 07:57:38 $
 */

package physis.core.genotype;

import physis.core.iterator.DigitalOrganismIterator;
import physis.core.DigitalOrganism;
import physis.log.Log;


import java.util.Hashtable;
import java.util.Iterator;
/**
 * This class represents a snapshot about the genetic structure of the population
 * in a timepoint.
 * It's dependent on the GeneBank.
 */
public class GeneticSnapshot{
    private DigitalOrganismIterator orgs;
    private Hashtable snapshot = new Hashtable();
    private double dominant_fitness;
    private DigitalOrganism dominant_org;
    
    public GeneticSnapshot(DigitalOrganismIterator orgs_){
        orgs = orgs_;
    }
    
    /**
     * Takes a snapshot by iterating through the whole populaion. Only the fertile organisms
     * are gathered.
     */
    public void takeASnapshot(){
	snapshot.clear();
        if (GeneBank.isEmpty()){Log.error("Genebank is empty. Genetic snapshot cannot be taken!"); return;}
	DigitalOrganism org;
	Genome g;
	Data d;
	String name;
	orgs.reset();
	while(orgs.hasNext()){
	    org = orgs.next();
	    if (org.isFertile()){
		g = org.getGenome();
		name = GeneBank.getGenomeName(g);
		if (name ==null){continue;}
		d = (Data)snapshot.get(name);
		if (d == null){
		    d = new Data();
		    d.number_of_occurence = 1;
		    d.an_org = org;
		    snapshot.put(name, d);
		}
		else{
		    d.number_of_occurence++;
		    if (d.an_org.getFitness() < org.getFitness()){
			d.an_org = org;
		    }
		}
	    }
	}
	searchForDominant();
    }
    
    public int getSnapshotSize(){return snapshot.size(); }
    
    
    public Genome getDominantGenotype(){
	return dominant_org.getGenome();
    }
    
    public double getDominantFitness(){
	return dominant_fitness;
    }
    
    public int getAbundance(String genotype_name){
        Data d = (Data) snapshot.get(genotype_name);
	if (d == null){
	    return 0;
	}
	else{
	    return d.number_of_occurence;
	}
    }
    
    private void searchForDominant(){
	if (snapshot.isEmpty()){
	    Log.status("No genetic information. It's too early or life vanished.");
	    return;
	}
	Iterator i = snapshot.values().iterator();
	Data d = null;
	int max = 0;
	Data dom = null;
	while (i.hasNext()){
	    d = (Data) i.next();
	    if (max < d.number_of_occurence){
		dom = d;
		max = dom.number_of_occurence;
	    }
	}
	dominant_fitness = dom.an_org.getFitness();
	dominant_org = dom.an_org;
    }
    
    /** Small inner class for values in the snapshot hashtable. */
    class Data{
        int number_of_occurence;
	DigitalOrganism an_org;
    }
    
    
}
