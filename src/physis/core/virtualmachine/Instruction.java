/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Instruction.java,v 1.4 2000/10/30 17:09:01 sirna Exp $
 * $Revision: 1.4 $
 * $Date: 2000/10/30 17:09:01 $
 */
package physis.core.virtualmachine;

import java.io.Serializable;

/**
 * This class represents one instruction for a virtual machine. Instructions are coded as positive integers.
 * Instructions have mnemonics just like in assembly languages. The instructions can be categorized into types. (copy, biological, nop...)
 * Instructions may have operands (although in evolvable languages they are not preferred).
 * @stereotype immutable, flyweight
 * Flyweight means as design patterns: Since the instances are immutable, they can be shared by all codetapes in the running VM.
 */
public class Instruction implements Serializable {
    /**
     * Actually it is the name of the instruction. It is only for people, because we can't read and write machine-code consisting number-like instructions.
     */
    private String instruction_mnemonic;
    
    /**
     * Every instruction has a type. (like flow-control, math-op). The typenames are stored in the InstructionSet, this is only an identifier.
     */
    private int type;
    
    /**
     * This is actually the main identifier of the instruction (the instruction itself). For performance reasons it is directly available in the package.
     */
    final short inst_code;
    
    /**
     * Tells whether the instruction has operand(s) or not. For performance reasons it is directly available in the package.
     */
    final boolean hasOperand;
    
    /**
     * The number of operands.
     */
    private int number_of_operands;
    
    /**
     * This class is immutable, which means that you can not change the state of an instance after you created. That's why the constructor needs all the information  about the instruction.
     */
    public Instruction(int type, short code, String mnemonic, int number_of_operands) {
        this.type = type;
        this.inst_code = code;
        this.instruction_mnemonic = mnemonic;
	this.number_of_operands = number_of_operands;
	if (number_of_operands > 0) { hasOperand = true; }
	else {hasOperand = false;}
    }
    
    /**
     * Returns the mnemonic (name) of the instruction.
     */
    public String getMnemonic(){
	return instruction_mnemonic;
    }
    
    /** Returns the type identifier. */
    public int getType() {
        return type;
    }
    
    /** Returns the number of operands. */
    public int getNumberOfOperands(){
        return number_of_operands;
    }
    
    public short getInstructionCode() { return inst_code; }
    
    public boolean equals(Object o){
        return inst_code == ( (Instruction) o).inst_code;
    }
    
}
