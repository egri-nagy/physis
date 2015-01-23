/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: GeneticCodeTapeFactory.java,v 1.3 2000/10/31 18:30:16 sirna Exp $
 * $Revision: 1.3 $
 * $Date: 2000/10/31 18:30:16 $
 */
package physis.core.virtualmachine;

import physis.core.Configuration;

import physis.log.Log;

public class GeneticCodeTapeFactory{
    
    private final static String POOL_SIZE = "pool_size";
    public static void init(){
	String tmp = Configuration.getProperty(POOL_SIZE);
	if (tmp != null){
	    try{
		int pool_size = Integer.parseInt(tmp);
		SoupGeneticCodeTape.pool = new short[pool_size];
		SoupGeneticCodeTape.attributes = new byte[pool_size];
	    }catch(NumberFormatException nfe){
	        Log.error(nfe);
	    }
	}
	else{
	    is_cell = true;
	}
    }
    
    private static boolean is_cell;
    
    public static GeneticCodeTape getGeneticCodeTape(String filename){
	if (is_cell){
	    return new CellGeneticCodeTape(filename);
	}
	else{
	    return new SoupGeneticCodeTape(filename);
	}
    }
    
    public static GeneticCodeTape getGeneticCodeTape(short[] code, byte[] attribs){
	if (is_cell){
	    return new CellGeneticCodeTape(code, attribs);
	}
	else{
	    return null;
	}
	
    }
}
