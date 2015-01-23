/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: SoupGeneticCodeTape.java,v 1.6 2001/07/06 08:54:55 sirna Exp $
 * $Revision: 1.6 $
 * $Date: 2001/07/06 08:54:55 $
 */
package physis.core.virtualmachine;

import physis.core.Configuration;
import physis.core.PHYSIS;
import physis.core.genotype.Genome;
import physis.log.Log;

import java.io.*;
import java.util.*;

/**
 * All organisms share one large memory pool. This allows many interactions
 * between them. (Tierra-like)
 */
public class SoupGeneticCodeTape implements GeneticCodeTape{
    private static InstructionSet instruction_set = InstructionSet.getInstance();
    private static short blank_inst = instruction_set.getBlankInstrunction().inst_code;
    
    
    /** This is the big memory pool. Only one could exist. */
    protected static short[] pool;
    /** The attributes array for the memory pool. */
    protected static byte[] attributes;
    
    /** The location of this codetape in the big pool. Pointer to the first instruction. */
    protected int loc;
    /** The size of the codetape. The position of the last instruction: loc + size - 1. */
    protected int size;
    protected int allocated_size;
    protected boolean alreadyallocated;
    
    /** The child's position. */
    protected int child_loc;
    /** The child's size */
    protected int child_size;
    
    /**
     * It reads the instructions from a file.The first organism is created this way.(One instruction per line,
     * lines beginning with # are discarded.
     */
    public SoupGeneticCodeTape(String filename) {
        try{
	    loc = PHYSIS.environment.getRandomness().nextInt(pool.length);
	    BufferedReader br = new BufferedReader(new FileReader(filename));
	    String buffer = br.readLine();
	    Vector v = new Vector();
	    while (buffer != null){
		if ((!buffer.startsWith("#")) && (!buffer.trim().equals(""))){
		    try{
			v.addElement(new Short(buffer));
		    }catch(NumberFormatException nfe){
		        v.addElement(new Short(instruction_set.getInstructionByName(buffer).getInstructionCode()));
		    }
		}
		buffer = br.readLine();
	    }
	    size = v.size();
	    Enumeration e = v.elements();
	    int i = 0;
	    while (e.hasMoreElements()) {
		this.write(i++, ((Short)e.nextElement()).shortValue());
	    }
        }catch (IOException ioe){
	    Log.error(" while reading codes from " + filename + "  " + ioe.getMessage());
        }
    }
    
    
    SoupGeneticCodeTape(int loc_, int size_){
        loc = loc_;
	size = size_;
    }
    
    /**
     * It gives the instruction on the specified position. SIDE EFFECT!!! The executed flag is set!
     */
    public short read(int position) {
	int pos = position + loc;
	while (pos < 0){
	    pos += pool.length;
	}
	pos = pos % pool.length;
	
	//get the instruction
        return pool[pos];
    }

    /** Just returns the value. If the postion parameter is outside of the whole pool then blank is returned.
     *  SIDE EFFECT!!! The executed flag is set!
     */
    public short fetchInst(int position){
        //calculating the absolute address
	int pos = position + loc;
	if ((pos < 0) || (pos >= pool.length)){
	    return blank_inst;
	}
	else{
	    attributes[pos] = (byte)(attributes[pos] | EXECUTED);
	    return pool[pos];
	}
    }
    
    public void write(int position, short new_value){
	int pos = position + loc;
	while (pos < 0){
	    pos += pool.length;
	}
	pos = pos % pool.length;
        pool[pos] = new_value;
    }
    
    
    public void clearExecutedFlag(int position){
	int pos = position + loc;
	while (pos < 0){
	    pos += pool.length;
	}
	pos = pos % pool.length;
	
        attributes[pos] = (byte) (attributes[pos] & (~EXECUTED));
    }
    
    
    public boolean isAllocationPossible(int numberof_newcells){
        return !alreadyallocated
	    &&  ((Configuration.getMinAllocationRatio() * size) < numberof_newcells )
	    &&  ((Configuration.getMaxAllocationRatio() * size) > numberof_newcells );
        
    }
    
    
    public void allocate(int numberof_newcells) {
        allocated_size = numberof_newcells;
	alreadyallocated = true;
    }
    
    public boolean isAllocated() { return alreadyallocated; }
    
    public boolean isProliferationPossible(){
        return alreadyallocated;
    }
    
    public GeneticCodeTape divide(){
	alreadyallocated = false;
	//Log.status("dic" + child_loc + " in divide " + child_size);
	SoupGeneticCodeTape child_tape =  new SoupGeneticCodeTape(child_loc, child_size);
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
	
	return child_tape;
    }
    
    public void mutate(int position) {
	int pos = position + loc;
	while (pos < 0){
	    pos += pool.length;
	}
	pos = pos % pool.length;
	
        pool[pos] = instruction_set.getInstructionCodeRandomly();
        attributes[pos] = (byte)(attributes[pos] | MUTATED);
    }
    
    
    public boolean isMutated(int position) {
	int pos = position + loc;
	while (pos < 0){
	    pos += pool.length;
	}
	pos = pos % pool.length;
	return MUTATED == (attributes[pos] & MUTATED);
    }
    public boolean isCopied(int position) {
	int pos = position + loc;
	while (pos < 0){
	    pos += pool.length;
	}
	pos = pos % pool.length;
	return COPIED == (attributes[pos] & COPIED);
    }
    public boolean isExecuted(int position) {
	int pos = position + loc;
	while (pos < 0){
	    pos += pool.length;
	}
	pos = pos % pool.length;
	return EXECUTED == (attributes[pos] & EXECUTED);
    }
    
    public boolean copy(int source, int destination){
	int src = loc + source;
	while (src < 0){
	    src = pool.length + src;
	}
	src = src % pool.length;
	
	int dest = loc + destination;
	while (dest < 0){
	    dest = pool.length + dest;
	}
	dest = dest % pool.length;
	if (PHYSIS.environment.copyShouldBeMutated()){
	    pool[dest] = instruction_set.getInstructionCodeRandomly();
	    attributes[dest] = (byte)(attributes[dest] | MUTATED | COPIED);
	}
	else{
	    pool[dest] = pool [source];
	    attributes[dest] = (byte) (attributes[dest] | COPIED);
	}
        return true;
    }
    
    public boolean blockCopy(int source, int destination, int length){
	int src = loc + source;
	while (src < 0){
	    src = pool.length + src;
	}
	src = src % pool.length;
	
	int dest = loc + destination;
	while (dest < 0){
	    dest = pool.length + dest;
	}
	dest = dest % pool.length;
	
	if (alreadyallocated){
	    child_loc = dest;
	    child_size = length;
	}
	
	for (int i = 0; i < length; i++){
	    if (PHYSIS.environment.copyShouldBeMutated()){
		pool[dest] = instruction_set.getInstructionCodeRandomly();
		attributes[dest] = (byte)(attributes[dest] | MUTATED | COPIED);
	    }
	    else{
		pool[dest] = pool [src];
		attributes[dest] = (byte) (attributes[dest] | COPIED);
	    }
	    dest = (++dest) % pool.length;
	    src = (++src) % pool.length;
	}
        return true;
    }
    
    /**
     * Inserts an instruction on a random position.
     */
    public void insert(){
	//int pos = ((PHYSIS.environment.getRandom().nextInt(size)) + loc) % pool.length;
	//throw new RuntimeException("not implemented!!!");
	
    }
    
    /**
     * Removes an instruction from a random position.
     */
    public void delete(){
	//throw new RuntimeException("not implemented!!!");
	
    }
    
    public int getSize(){ return size; }
    
    public boolean contains(short instcode){
        for (int i = 0; i < size; i++){
	    if (pool[(loc + i) % pool.length] == instcode) return true;
        }
        return false;
    }
    
    public int searchForward(int start, short code, int depth){
        int pos = (loc + start) % pool.length;
	for (int i = 0; i < depth; i++){
	    int j = 0;
	    while (pool[pos] != code ) {
		pos = (pos + 1) % pool.length;
		if ((j++) > 400000){
		    Log.status(j + "");
		}
	    }
        }
	return pos;
    }
    
    public int searchBackward(int start, short code, int depth){
        int pos = (loc + start) % pool.length;
	for (int i = 0; i < depth; i++){
	    int j = 0;
	    while (pool[pos] != code ) {
		pos = (pos + 1);
		if ((j++) > 400000){
		    Log.status(j + "");
		}
		
		if (pos < 0) {
		    pos = pool.length - 1;
		}
	    }
        }
	return pos;
    }
    
    
    /** The effective length can be calculated by the number of executed instructions. */
    public int calculateEffectiveLength(){
        //Log.status("eff_length called");
        int num = 0;
	int pos = loc;
        for (int i = 0; i < size; i++){
	    pos = (++pos) % pool.length;
	    if ( (attributes[pos] & EXECUTED) != 0) num++;
        }
        //Log.status("eff_length" + num);
        return num;
    }
    
    
    /**
     * Returns the genetic information from the codetape.
     */
    public Genome getGenome(){
	short[] genes = new short[size];
	for (int i = 0; i < size ; i ++){
	    genes[i] = read(i);
	}
	
        return new Genome(genes);
    }
    
    /**
     * Returns the genetic information from the codetape.
     */
    public Genome getChildGenome(){
	short[] genes = new short[child_size];
	for (int i = 0; i < child_size ; i ++){
	    genes[i] = getChildInstruction(i);
	}
	
        return new Genome(genes);
    }
    
    protected short getChildInstruction(int position){
	int pos = position + loc;
	pos = pos % pool.length;
	//get the instruction
        return pool[pos];
	
    }
    
}
