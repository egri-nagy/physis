/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: GeneticCodeTape.java,v 1.9 2001/06/13 07:34:16 sirna Exp $
 * $Revision: 1.9 $
 * $Date: 2001/06/13 07:34:16 $
 */
package physis.core.virtualmachine;

import physis.core.genotype.Genome;

/**
 * The abstraction of the genetic code. It remains a circular tape with assembly like
 * instructions and operands on it but it has some biological features needed for cell-division like processes,
 * activity registration and other access functions.
 * !!It should be a common interface for both soup and cell-like codetapes!!
 */
public interface GeneticCodeTape extends CodeTape{

    //the ATTRIBUTES for stored instructions
    /** Instruction-attribute: the instruction is executed at least once. */
    byte EXECUTED = 1;
    /** Instruction-attribute: the instruction is a copied one. */
    byte COPIED = 2;
    /** Instruction-attribute: the instruction was mutated. */
    byte MUTATED = 4;
    /** Instruction-attribute: the instruction is executed or copied. */
    byte EXECUTED_OR_COPIED = EXECUTED | COPIED;
    
    /**
     * Simply returns the instruction or operand on the specified position.
     * <br> The position becomes EXECUTED. <br>
     * This method isn't failsafe like <code>read</code> and <code>write</code>. The virtualmachine has to take care of the proper position.
     * It provides only quick access for the executable part of the code.
     */
    public short fetchInst(int position);
    
    //SPACE-ALLOCATION
    
    /**
     * The size must be in a predefined range,
     * otherwise the allocation fails.
     */
    public boolean isAllocationPossible(int numberof_newcells);

    /**
     * It's a biological operator. This should happen before the cell division. (making room, preparating or something like that)
     * CELL: It allocates instruction cells at the end of the codetape. One allocation may occur between divides.
     * It also allocates the information arrays (iscopied...etc.)
     * SOUP: Because of the one large memory pool (soup) there's no space allocation. In this case it's only the declaration of
     * the size of the code block to be copied.
     */
    public void allocate(int numberof_newcells);
    
    /**
     * Returns true if the cell already allocated space for the spawn.
     */
    public boolean isAllocated();

    //CELL-DIVISION
    
    /**
     * Checks whether the organism is able to spawn (are there enough copied instructions?).
     */
    public boolean isProliferationPossible();
       
    /** It divides the codetape. The <code>Environment</code> is responsible for palcing the child,
     * the codetape only returns an other one.
     */
    public GeneticCodeTape divide();


    
    /**
     * Returns true if the codetape contains the specified instruction.
     * @param instcode The searched instruction.
     */
    public boolean contains(short instcode);
    
    /**
     * Answers the question: is the positionth instruction mutated?
     */
    public boolean isMutated(int position);

    /**
     * Answers the question: is the positionth instruction copied?
     */
    public boolean isCopied(int position);
    
    /**
     * Answers the question: is the positionth instruction executed?
     */
    public boolean isExecuted(int position);
    
    public void clearExecutedFlag(int position);
    /**
     * Mutate the specified instruction.
     */
    public void mutate(int position);
    /**
     * Inserts an instruction on a random position.
     */
    public void insert();
    /**
     * Removes an instruction from a random position.
     */
    public void delete();
    
    /** Copies an instruction from source position to destination position. Returns true if the copy was succesfull. */
    public boolean copy(int source, int destination);
    
    /** Copies an instruction from source position to destination position. Returns true if the copy was succesfull. */
    public boolean blockCopy(int source, int destination, int length);

    /** The effective length can be calculated by the number of executed instructions. The executed nop instructions don't count. */
    public int calculateEffectiveLength();

    /** Returns the absolute size of the codetape.(number of contained instructions) */
    public int getSize();

    //GENETIC INFORMATION
    
    /**
     * Returns the genetic information from the codetape.
     */
    public Genome getGenome();
    
    /**
     * Returns the genetic information from the child-codetape.
     */
    public Genome getChildGenome();
}
