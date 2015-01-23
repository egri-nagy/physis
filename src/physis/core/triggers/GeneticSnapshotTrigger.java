/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: GeneticSnapshotTrigger.java,v 1.3 2001/06/05 14:02:47 sirna Exp $
 * $Revision: 1.3 $
 * $Date: 2001/06/05 14:02:47 $
 */
package physis.core.triggers;

import physis.core.PHYSIS;
import physis.core.Configuration;
import physis.core.genotype.GeneticSnapshot;
import physis.log.Log;

import java.util.StringTokenizer;
import java.io.*;

public class GeneticSnapshotTrigger extends  DataExtractTrigger{
    private String filename;
    private GeneticSnapshot snapshot;

    public void setParameters(StringTokenizer params){
	filename = Configuration.getPhysisHome() + params.nextToken();
	//removes if the filename exists
	File f = new File(filename);
	if (f.exists()){f.delete();}
	snapshot = new GeneticSnapshot(PHYSIS.getInstance().getPopulation().getAllOrganism());
    }

    public void execute(){
	try{
	    PrintWriter pw = new PrintWriter(
		new FileWriter(filename,true));
	    snapshot.takeASnapshot();
	    pw.write(snapshot.getDominantFitness()+"\n");
	    pw.close();
	    if (is_periodical){
		adjustPeriodicalSchedule();
	    }

	}catch(IOException ioe){Log.error(ioe);}
    }

    public String toString(){
        return "GeneticSnapshotTrigger: writes information to file" + filename;
    }
}
