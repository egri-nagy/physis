/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: CellGeneticCodeTape.java,v 1.6 2003/01/10 12:19:02 sirna Exp $
 * $Revision: 1.6 $
 * $Date: 2003/01/10 12:19:02 $
 */
package physis.core.virtualmachine;

import physis.core.Configuration;
import physis.core.PHYSIS;
import physis.core.genotype.Genome;
import physis.log.Log;

import java.io.*;
import java.util.*;

/**
 * Cell-like implementations of the genetic codetape. For each organism there's
 * a separated cell. (Avida-like)
 */
public class CellGeneticCodeTape implements GeneticCodeTape{
    
    private static InstructionSet instruction_set = InstructionSet.getInstance();
    private static short blank_inst = instruction_set.getBlankInstrunction().inst_code;
    
    /**
     * The instructions are represented with a thin class called InstructionCode.
     * Due memory saving and performance considerations it stores only the instructioncode,
     * and this code is available directly without method invocation.
     */
    private short[] memory;
    
    /**
     * When the organism is pregnant the child's code is being developed here.
     */
    private short[] child;
    
    /**
     * It's a flag which indicates whether an allocation has happened or not.
     */
    private boolean alreadyallocated;
    
    /**
     * Each byte represents the attributes of an instruction. Each bit represents one attribute. (executed, copied, mutated..)
     */
    private byte[] attributes;
    private byte[] childattributes;
    
    
    /**
     * It reads the instructions from a file.The first organism is created this way.(One instruction per line,
     * lines beginning with # are discarded.
     */
    public CellGeneticCodeTape(String filename) {
        try{
	    BufferedReader br = new BufferedReader(new FileReader(filename));
	    String buffer = br.readLine();
	    Vector v = new Vector();
	    while (buffer != null){
		if ((!buffer.startsWith("#")) && (!buffer.trim().equals(""))){
		    try{
			if (buffer.indexOf('#') != -1){
			    v.addElement(new Short(buffer.substring(0,buffer.indexOf('#')-1)));
			}
			else{
			    v.addElement(new Short(buffer));
			}
		    }catch(NumberFormatException nfe){
			if (buffer.indexOf('#') != -1){
			    v.addElement(new Short(instruction_set.getInstructionByName(buffer.substring(0,buffer.indexOf('#')-1).trim()).getInstructionCode()));
			}
			else{
			    v.addElement(new Short(instruction_set.getInstructionByName(buffer.trim()).getInstructionCode()));
			}
		    }
		}
		buffer = br.readLine();
	    }
	    memory = new short[v.size()];
	    attributes = new byte[v.size()];
	    Enumeration e = v.elements();
	    int i = 0;
	    while (e.hasMoreElements()) { memory[i++] = ((Short)e.nextElement()).shortValue();}
        }catch (IOException ioe){
	    Log.error(" while reading codes from " + filename + "  " + ioe.getMessage());
        }
    }
    
    /**
     * Creates codetape from instructionarray.
     */
    CellGeneticCodeTape(short[] instructions, byte[] attribs){
        memory = instructions;
        attributes = attribs;
    }
    
    
    /**
     * It gives the instruction on the specified position. If the index is out of bounds
     * then it will be mapped into the proper range. (child after parent)
     */
    public short read(int position) {
	position = Math.abs(position % (memory.length + ( (child == null) ? 0 : child.length)));
        if (position < memory.length){
	    return memory[position];
	}
	else{
	    return child[position - memory.length];
	}
    }
    
    /**
     * The position is mapped into the proper range. (child after parent)
     */
    public void write(int position, short new_value){
	position = Math.abs(position % (memory.length + ( (child == null) ? 0 : child.length)));
        if (position < memory.length){
	    memory[position] = new_value;
	}
	else{
	    child[position - memory.length] = new_value;
	    childattributes[position - memory.length] |= COPIED;
	    if (PHYSIS.environment.copyShouldBeMutated()){
		child[position - memory.length] = instruction_set.getInstructionCodeRandomly();
		childattributes[position - memory.length] = (byte) (childattributes[position - memory.length] | MUTATED);
		
	    }
	    
	}
    }
    
    /** Just returns the value. If the postion parameter is outside of the main (parent) code blank is returned.
     *  SIDE EFFECT!!! The executed flag is set!
     */
    public short fetchInst(int position){
	if ((position < 0) || (position >= memory.length)){
	    return blank_inst;
	}
	else{
	    attributes[position] = (byte)(attributes[position] | EXECUTED);
	    return memory[position];
	}
    }
    
    
    /**
     * It's a biological operator. It allocates instruction cells at the end of the codetape. The size must be more than half and less than the current size, otherwise the allocation fails. One allocation may occur between divides.
     * <br>
     * It also allocates the information arrays (iscopied...etc.)
     */
    public synchronized void allocate(int numberof_newcells) {
        //if it is allocated then no other allocation
        if (!alreadyallocated){
	    child = new short[numberof_newcells];
	    for (int i = 0; i < numberof_newcells; i++){
	        child[i] = blank_inst;
	    }
	    childattributes = new byte[numberof_newcells];
	    alreadyallocated = true;
        }
    }
    public boolean isAllocated() { return alreadyallocated; }
    
    /**
     * Checks whether the organism is able to spawn.
     */
    public boolean isProliferationPossible(){
        return alreadyallocated
	    &&  ((Configuration.getMinProliferationRatio() * memory.length) < getNumberOfCopiedInstructionsInChild() );
        
    }
    
    public boolean isAllocationPossible(int numberof_newcells){
        return !alreadyallocated
	    &&  ((Configuration.getMinAllocationRatio() * memory.length) < numberof_newcells )
	    &&  ((Configuration.getMaxAllocationRatio() * memory.length) > numberof_newcells );
        
    }
    
    
    /** It divides the codetape.*/
    public synchronized GeneticCodeTape divide() {
        CellGeneticCodeTape child_tape = new CellGeneticCodeTape(child, childattributes);
        //here comes the divide mutation stuff...
        if (PHYSIS.environment.divideShouldBeMutated()){
	    child_tape.mutate(PHYSIS.environment.getRandomness().nextInt(child_tape.getSize()));
        }
        if (PHYSIS.environment.shouldBeInserted()){
	    child_tape.insert();
        }
        if (PHYSIS.environment.shouldBeDeleted()){
	    child_tape.delete();
        }
        child = null; childattributes = null;
	alreadyallocated = false;
        return child_tape;
    }
    
    /**
     * Returns true if the codetape contains the specified instruction.
     * @param instcode The searched instruction.
     */
    public boolean contains(short instcode){
        for (int i = 0; i < memory.length; i++){
	    if (memory[i] == instcode) return true;
        }
        return false;
    }
    
    /**
     * Answers the question: is the positionth instruction mutated?
     */
    public boolean isMutated(int position) {
        return MUTATED == (attributes[position] & MUTATED);
    }
    public boolean isCopied(int position) {
        return COPIED == (attributes[position] & COPIED);
    }
    public boolean isExecuted(int position) {
        return EXECUTED == (attributes[position] & EXECUTED);
    }
    
    public void clearExecutedFlag(int position){
        attributes[position] = (byte) (attributes[position] & (~EXECUTED));
    }
    
    /**
     * Mutate the specified instruction.
     */
    public void mutate(int position) {
        memory[position] = instruction_set.getInstructionCodeRandomly();
        attributes[position] = (byte)(attributes[position] | MUTATED);
    }
    
    /**
     * Inserts an instruction on a random position.
     */
    public void insert(){
        short[] newmemory = new short[memory.length + 1];
        byte[] newattributes = new byte[attributes.length + 1];
        int pos = PHYSIS.environment.getRandomness().nextInt(newmemory.length);
        
        if (pos == 0){
	    newmemory[0] = instruction_set.getInstructionCodeRandomly();
	    newattributes[0] = MUTATED;
	    
	    System.arraycopy(memory,0,newmemory,1,memory.length);
	    System.arraycopy(attributes,0,newattributes,1,attributes.length);
        }
        else if (pos == newmemory.length - 1){
	    newmemory[pos] = instruction_set.getInstructionCodeRandomly();
	    newattributes[pos] = MUTATED;
	    
	    System.arraycopy(memory,0,newmemory,0,memory.length);
	    System.arraycopy(attributes,0,newattributes,0,attributes.length);
	    
        }
        else{
	    System.arraycopy(memory,0,newmemory,0,pos);
	    System.arraycopy(attributes,0,newattributes,0,pos);
	    
	    newmemory[pos] = instruction_set.getInstructionCodeRandomly();
	    newattributes[pos] = MUTATED;
	    
	    System.arraycopy(memory, pos, newmemory, (pos + 1), newmemory.length - (pos + 1));
	    System.arraycopy(attributes,pos,newattributes, (pos + 1), newattributes.length - (pos + 1));
        }
        
        memory = newmemory;
        attributes = newattributes;
    }
    
    /**
     * Removes an instruction from a random position.
     */
    public void delete(){
        short[] newmemory = new short[memory.length - 1];
        byte[] newattributes = new byte[attributes.length - 1];
        int pos = PHYSIS.environment.getRandomness().nextInt(newmemory.length);
        
        if (pos == 0){
	    newmemory[0] = instruction_set.getInstructionCodeRandomly();
	    newattributes[0] = MUTATED;
	    
	    System.arraycopy(memory,1,newmemory,0,newmemory.length);
	    System.arraycopy(attributes,1,newattributes,0,newattributes.length);
        }
        else if (pos == newmemory.length - 1){
	    
	    System.arraycopy(memory,0,newmemory,0,newmemory.length);
	    System.arraycopy(attributes,0,newattributes,0,newattributes.length);
	    
        }
        else{
	    System.arraycopy(memory,0,newmemory,0,pos);
	    System.arraycopy(attributes,0,newattributes,0,pos);
	    
	    
	    System.arraycopy(memory, pos + 1, newmemory, pos, newmemory.length - pos);
	    System.arraycopy(attributes,pos + 1,newattributes, pos , newattributes.length - pos);
        }
        
        memory = newmemory;
        attributes = newattributes;
        
    }
    
    /** Copies an instruction from source cell to destination cell. Returns true if the copy was succesfull. */
    public boolean copy(int source, int destination) {
	//negative values are invalid - do nothing and return
	if ((source < 0) || (destination < 0)){ return false;}
	
	if (child != null){
	    //we need the full length of the codetape
	    int full_length = memory.length + child.length;
	    //if the index is over the range then it's cycled back
	    source = source % full_length;
	    destination = destination % full_length;
	    
	    if (destination < memory.length){
		if (source < memory.length){
		    memory[destination] = memory[source];
		}
		else{
		    memory[destination] = child[source - memory.length];
		}
		attributes[destination] = (byte)(attributes[destination] | COPIED);
	    }
	    else{
		if (source < memory.length){
		    child[destination - memory.length] = memory[source];
		}
		else{
		    child[destination - memory.length] = child[source - memory.length];
		}
		childattributes[destination - memory.length] = (byte)(childattributes[destination - memory.length] | COPIED);
		if (PHYSIS.environment.copyShouldBeMutated()){
		    child[destination - memory.length] = instruction_set.getInstructionCodeRandomly();
		    childattributes[destination - memory.length] = (byte) (childattributes[destination - memory.length] | MUTATED);
		    
		}
	    }
	}
	else{
	    //if the child is null then the only possible case is the parent to parent copy
	    if (destination < memory.length){
		if (source < memory.length){
		    memory[destination] = memory[source];
		}
		attributes[destination] = (byte)(attributes[destination] | COPIED);
	    }
	    
	}
	return true;
    }
    
    public boolean blockCopy(int source, int destination, int length){
        throw new RuntimeException("not implemented!!!");
    }
    
    /** The effective length can be calculated by the number of executed instructions.*/
    public int calculateEffectiveLength(){
        int num = 0;
        for (int i = 0; i < memory.length; i++){
	    if ((attributes[i] & EXECUTED) != 0) num++;
        }
        
        return num;
    }
    
    /** Returns the size of the codetape. */
    public int getSize() { return memory.length; }
    
    /**
     * Returns the genetic information from the codetape.
     */
    public Genome getGenome(){
	short[] genes = new short[memory.length];
	for (int i = 0; i < memory.length ; i ++){
	    genes[i] = memory[i];
	}
	
        return new Genome(genes);
    }
    
    /**
     * Returns the genetic information from the codetape.
     */
    public Genome getChildGenome(){
	short[] genes = new short[child.length];
	for (int i = 0; i < child.length ; i ++){
	    genes[i] = child[i];
	}
	
        return new Genome(genes);
    }
    
    public static GeneticCodeTape getRandomTape(int length){
        short[] tape = new short[length];
	byte[] attribs = new byte[length];
	for (int i=0; i < length; i++){
	    tape[i] = instruction_set.getInstructionCodeRandomly();
	}
	return new CellGeneticCodeTape(tape, attribs);
    }
    
    /** Returns the string representation of the codetape. One instruction per line. */
    public String toString(){
        if (memory == null) return "No real codetape.";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < memory.length; i++){
	    try{
		sb.append(instruction_set.getInstructionByCode(memory[i]).getMnemonic() + "\n");
	    }catch(Exception exc){
		sb.append(memory[i]);
	    }
        }
        
        return sb.toString();
    }
    
    private int getNumberOfCopiedInstructionsInChild(){
        int num = 0;
        for (int i = 0; i < child.length; i++){
	    if ( ((childattributes[i] & COPIED) != 0) ) num++;
        }
        return num;
    }
}
