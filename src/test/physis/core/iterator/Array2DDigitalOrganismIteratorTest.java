/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Array2DDigitalOrganismIteratorTest.java,v 1.4 2000/08/09 14:10:09 sirna Exp $
 * $Revision: 1.4 $
 * $Date: 2000/08/09 14:10:09 $
 */
package test.physis.core.iterator;

import physis.core.*;
import physis.core.iterator.*;
import physis.log.Log;

public class Array2DDigitalOrganismIteratorTest{
    //test
    public static void main(String[] args){
        try{     Configuration.init(args[0]);
            DigitalOrganism[][] orgs = new DigitalOrganism[6][];
            for (int i = 0; i < orgs.length; i++){
                orgs[i] = new DigitalOrganism[6];
                for (int j = 0; j < orgs[i].length; j++){
                    orgs[i][j] = new DigitalOrganismImpl();
                    orgs[i][j].setPositionInfo((i + " " + j));
                }
            }
            
            Array2DDigitalOrganismIterator it = new Array2DDigitalOrganismIterator(orgs);
            while (it.hasNext()) { System.out.println(it.next().getPositinInfo());
            }
        } catch(Exception e){
            Log.error(e);
        }
        
    }
    
}
