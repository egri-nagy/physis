/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Revision: 1.8 $
 * $Date: 2001/04/05 16:05:48 $
 */

package physis.core.genotype;

import physis.core.virtualmachine.InstructionSet;
import physis.core.virtualmachine.Instruction;

/**
 * The genetic information for persistent storage. The reference to the coresponding instructionset should be stored as well.
 */
public class Genome {
    /** The array containing the instruction codes. */
    private short[] genome;
    /** We store the hashcode for performance reasosns. */
    private int hashcode;
    
    //for hash-generating
    private static int[] primes = { 3, 5, 7, 11, 13, 17, 19, 23, 27, 29, 31, 37, 41, 47, 49, 51, 53, 57, 59, 61 };
    
    
    public Genome(short[] genes) {
        genome = genes;
    }
    
    /** Returns the string representation of the codetape. One instruction per line. */
    public String toString(){
        if (genome == null) return "It's not a real codetape.";
        InstructionSet is = InstructionSet.getInstance();
        StringBuffer sb = new StringBuffer();
        int i = 0;
	while (i < genome.length){
	    Instruction instr = is.getInstructionByCode(genome[i]);
	    if (instr != null){
		sb.append(instr.getMnemonic() + "\n");
		i++;
		int j = 0;
		while ((j < instr.getNumberOfOperands()) && (i < genome.length)){
		    sb.append(genome[i] + "\n");
		    j++;i++;
		}
	    }
	    else{
		sb.append(genome[i] + "\n");
		i++;
	    }
	}
	sb.append("\n");
	return sb.toString();
    }
    
    
    /**
     * This is really important because we don't want to store the same genome twice.
     */
    public boolean equals(Object o){
	short[] other = ((Genome) o).genome;
	
	//if size differs then not equal
	if (other.length != genome.length){
	    return false;
	}
	
	//if one instruction differs then not equal
	for (short i = 0; i < genome.length; i++){
	    if  (genome[i] != other[i]){
		return false;
	    }
	}
	return true;
    }
    
    public int size(){ return genome.length; }
    
    public int hashCode(){
	int code = 0;
	for (int i = 0 ; i < genome.length; i++){
	    code += genome[i] * primes[i % primes.length];
	}
	return code;
    }
    
}
