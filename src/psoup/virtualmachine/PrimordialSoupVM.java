/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: PrimordialSoupVM.java,v 1.6 2001/08/09 11:00:43 sirna Exp $
 * $Revision: 1.6 $
 * $Date: 2001/08/09 11:00:43 $
 */
package psoup.virtualmachine;

import physis.core.virtualmachine.VirtualMachine;
import physis.core.virtualmachine.PhysisVirtualMachine;
import physis.core.virtualmachine.GeneticCodeTape;
import physis.core.virtualmachine.InstructionSet;
import physis.core.PHYSIS;
import physis.core.event.ProliferationEvent;
import physis.core.genotype.Genome;
import physis.log.Log;

public class PrimordialSoupVM extends PhysisVirtualMachine{
    /** Size of the internal stack. (hardcoded 16, because it's the attribute of the architecture */
    public static final int STACK_SIZE = 16;
    /** Reference for instruction set in order to access it quickly. */
    private static InstructionSet instruction_set = InstructionSet.getInstance();
    
    //opcodes
    protected final short JUMP	= 0;
    protected final short SPWN = 1;
    protected final short PCR1 = 2;
    protected final short PCR2 = 3;
    protected final short PCR3 = 4;
    protected final short PCR4	= 5;
    protected final short R2R1	= 6;
    protected final short R3R1	= 7;
    protected final short R4R1	= 8;
    protected final short R1R2	= 9;
    protected final short R1R3	= 10;
    protected final short R1R4	= 11;
    protected final short ADD1	= 12;
    protected final short ADD2	= 13;
    protected final short ADD3	= 14;
    protected final short ADD4	= 15;
    protected final short SUB1	= 16;
    protected final short SUB2	= 17;
    protected final short SUB3	= 18;
    protected final short SUB4	= 19;
    protected final short R2M1	= 20;
    protected final short R3M1	= 21;
    protected final short R4M1	= 22;
    protected final short R1M2	= 23;
    protected final short R3M2	= 24;
    protected final short R4M2	= 25;
    protected final short M1R2	= 26;
    protected final short M1R3	= 27;
    protected final short M1R4	= 28;
    protected final short M2R1	= 29;
    protected final short M2R3	= 30;
    protected final short M2R4	= 31;
    protected final short JPZ1	= 32;
    protected final short JPZ2	= 33;
    protected final short JPZ3	= 34;
    protected final short JPZ4	= 35;
    protected final short NNR1	= 36;
    protected final short NNR2	= 37;
    protected final short NNR3	= 38;
    protected final short NNR4	= 39;
    protected final short INC1	= 40;
    protected final short INC2	= 41;
    protected final short INC3	= 42;
    protected final short INC4	= 43;
    protected final short DEC1	= 44;
    protected final short DEC2	= 45;
    protected final short DEC3	= 46;
    protected final short DEC4	= 47;
    protected final short JMPF	= 48;
    protected final short JMPB	= 49;
    protected final short PSHP	= 50;
    protected final short PSH1	= 51;
    protected final short PSH2	= 52;
    protected final short PSH3	= 53;
    protected final short PSH4	= 54;
    protected final short POP1	= 55;
    protected final short POP2	= 56;
    protected final short POP3	= 57;
    protected final short POP4	= 58;
    protected final short CLLF	= 59;
    protected final short CLLB	= 60;
    protected final short RETN	= 61;
    protected final short SCF1	= 62;
    protected final short SCF2	= 63;
    protected final short SCF3	= 64;
    protected final short SCF4	= 65;
    protected final short SCB1	= 66;
    protected final short SCB2	= 67;
    protected final short SCB3	= 68;
    protected final short SCB4	= 69;
    /* All opcodes below are no-ops */
    protected final short LBL0	= 70;
    protected final short LBL1	= 71;
    protected final short LBL2	= 72;
    protected final short LBL3	= 73;
    protected final short LBL4	= 74;
    protected final short LBL5	= 75;
    protected final short LBL6	= 76;
    protected final short LBL7	= 77;
    protected final short LBL8	= 78;
    protected final short LBL9	= 79;
    protected final short NOOP	= 80;
    
    //the structure of the VM
    //registers
    protected int R1;
    protected int R2;
    protected int R3;
    protected int R4;
    //internal stack
    protected int[] stack = new int[STACK_SIZE];
    //stack_pointer
    protected int SP = 0;
    
    /** instruction pointer */
    protected int IP;
    
    public PrimordialSoupVM(){
    }
    
    public void reset(){
        restart();
        gestation_time = GESTATION_TIME_INVALID;
    }
    
    public void restart(){
	R1 = 0;
	R2 = 0;
        R3 = 0;
	R4 = 0;
	SP = 0;
        setIP(0);
        counter = 0;
    }
    
    
    public void execute(){
        try{
	    //calls the method corresponding the instructioncode
	    switch ( tape.read(IP) ){
		case JUMP:{	//unconditional jump, IP relative jump specified the operand
			adjustIP(1);
			adjustIP(tape.read(IP));
		    }
		    break;
		    
		case SPWN :{	//spawning: source in R1, destination in R2, length in R3
			if (tape.isAllocationPossible(R3) && (R1 != R2)){
			    tape.allocate(R3);
			    tape.blockCopy(R1,R2,R3);
			    gestation_time = counter + R3;
			    PHYSIS.environment.proliferationPerformed(new ProliferationEvent(tape.divide(), bearer));
			}
			else{
			    tape.clearExecutedFlag(IP);
			    adjustIP(1);
			}
		    }
		    break;
		    
		case PCR1:{	//PC (IP) -> R1
			R1 = IP;
			adjustIP(1);
		    }
		    break;
		    
		case PCR2:{
			R2 = IP;
			adjustIP(1);
		    }
		    break;
		    
		case PCR3:{
			R3 = IP;
			adjustIP(1);
		    }
		    break;
		    
		case PCR4:{
			R4 = IP;
			adjustIP(1);
		    }
		    break;
		    
		case R2R1:{	//R2 -> R1
			R1 = R2;
			adjustIP(1);
		    }
		    break;
		    
		case R3R1:{
			R1 = R3;
			adjustIP(1);
		    }
		    break;
		    
		case R4R1:{
			R1 = R4;
			adjustIP(1);
		    }
		    break;
		    
		case R1R2:{
			R2 = R1;
			adjustIP(1);
		    }
		    break;
		    
		case R1R3:{
			R3 = R1;
			adjustIP(1);
		    }
		    break;
		    
		case R1R4:{
			R4 = R1;
			adjustIP(1);
		    }
		    break;
		    
		case ADD1:{	//The result is always in R1.
			R1 = R1 + R1;
			adjustIP(1);
		    }
		    break;
		    
		case ADD2:{
			R1 = R1 + R2;
			adjustIP(1);
		    }
		    break;
		    
		case ADD3:{
			R1 = R1 + R3;
			adjustIP(1);
		    }
		    break;
		    
		case ADD4:{
			R1 = R1 + R4;
			adjustIP(1);
		    }
		    break;
		    
		case SUB1:{
			R1 = R1 - R1;
			adjustIP(1);
		    }
		    break;
		    
		case SUB2:{
			R1 = R1 - R2;
			adjustIP(1);
		    }
		    break;
		    
		case SUB3:{
			R1 = R1 - R3;
			adjustIP(1);
		    }
		    break;
		    
		case SUB4:{
			R1 = R1 - R4;
			adjustIP(1);
		    }
		    break;
		    
		case R2M1:{	//R2 -> memory specified by R1
			tape.write(R1,(short) R2);
			adjustIP(1);
		    }
		    break;
		    
		case R3M1:{
			tape.write(R1,(short) R3);
			adjustIP(1);
		    }
		    break;
		    
		case R4M1:{
			tape.write(R1,(short) R4);
			adjustIP(1);
		    }
		    break;
		    
		case R1M2:{
			tape.write(R2,(short) R1 );
			adjustIP(1);
		    }
		    break;
		    
		case R3M2:{
			tape.write(R2,(short) R3);
			adjustIP(1);
		    }
		    break;
		    
		case R4M2:{
			tape.write(R2,(short) R4);
			adjustIP(1);
		    }
		    break;
		    
		case M1R2:{	//memory specified by R1 -> R2
			R2 = tape.fetchInst(R1);
			adjustIP(1);
		    }
		    break;
		    
		case M1R3:{
			R3 = tape.fetchInst(R1);
			adjustIP(1);
		    }
		    break;
		    
		case M1R4:{
			R4 = tape.fetchInst(R1);
			adjustIP(1);
		    }
		    break;
		    
		case M2R1:{
			R1 = tape.fetchInst(R2);
			adjustIP(1);
		    }
		    break;
		    
		case M2R3:{
			R3 = tape.fetchInst(R2);
			adjustIP(1);
		    }
		    break;
		    
		case M2R4:{
			R4 = tape.fetchInst(R2);
			adjustIP(1);
		    }
		    break;
		    
		case JPZ1:{
			adjustIP(1);
			if (R1 == 0) {
			    adjustIP(tape.read(IP));
			}
			else{
			    adjustIP(1);
			}
		    }
		    break;
		    
		case JPZ2:{
			adjustIP(1);
			if (R2 == 0) {
			    adjustIP(tape.read(IP));
			}
			else{
			    adjustIP(1);
			}
		    }
		    break;
		    
		case JPZ3:{
			adjustIP(1);
			if (R3 == 0) {
			    adjustIP(tape.read(IP));
			}
			else{
			    adjustIP(1);
			}
		    }
		    break;
		    
		case JPZ4:{
			adjustIP(1);
			if (R4 == 0) {
			    adjustIP(tape.read(IP));
			}
			else{
			    adjustIP(1);
			}
		    }
		    break;
		    
		case NNR1:{	//operand's value -> R1
			adjustIP(1);
			R1 = tape.read(IP);
			adjustIP(1);
		    }
		    break;
		    
		case NNR2:{
			adjustIP(1);
			R2 = tape.read(IP);
			adjustIP(1);
		    }
		    break;
		    
		case NNR3:{
			adjustIP(1);
			R3 = tape.read(IP);
			adjustIP(1);
		    }
		    break;
		    
		case NNR4:{
			adjustIP(1);
			R4 = tape.read(IP);
			adjustIP(1);
		    }
		    break;
		    
		case INC1:{
			R1++;
			adjustIP(1);
		    }
		    break;
		    
		case INC2:{
			R2++;
			adjustIP(1);
		    }
		    break;
		    
		case INC3:{
			R3++;
			adjustIP(1);
		    }
		    break;
		    
		case INC4:{
			R4++;
			adjustIP(1);
		    }
		    break;
		    
		case DEC1:{
			R1--;
			adjustIP(1);
		    }
		    break;
		    
		case DEC2:{
			R2--;
			adjustIP(1);
		    }
		    break;
		    
		case DEC3:{
			R3--;
			adjustIP(1);
		    }
		    break;
		    
		case DEC4:{
			R4--;
			adjustIP(1);
		    }
		    break;
		    
		case JMPF:{
			int jmpf_pos = IP;				//store the pos. of the JMPF instruction
			adjustIP(1);					//step to the operand
			short op = tape.read(IP);
			if (tape.contains(op)){
			    do{						//searching for the operand
				adjustIP(1);
			    }while (tape.fetchInst(IP) != op);
			}else{						//otherwise clear the flag for JMPF
			    tape.clearExecutedFlag(jmpf_pos);
			    tape.clearExecutedFlag(IP);
			    adjustIP(1);
			}
		    }
		    break;
		    
		case JMPB:{
			int jmpb_pos = IP;				//store the pos. of the JMPB instrutapeion
			adjustIP(1);					//step to the operand
			short op = tape.read(IP);
			if (tape.contains(op)){
			    adjustIP(-1);
			    do{						//searching for the operand
				adjustIP(-1);
			    }while (tape.fetchInst(IP) != op);
			}else{						//otherwise clear the flag for JMPB
			    tape.clearExecutedFlag(jmpb_pos);
			    tape.clearExecutedFlag(IP);
			    adjustIP(1);
			}
		    }
		    break;
		    
		case PSHP:{
			if (!isFull()){
			    push(IP);
			}
			else{
			    tape.clearExecutedFlag(IP);
			}
			adjustIP(1);
		    }
		    break;
		    
		case PSH1:{
			if (!isFull()){
			    push(R1);
			}
			else{
			    tape.clearExecutedFlag(IP);
			}
			adjustIP(1);
		    }
		    break;
		    
		case PSH2:{
			if (!isFull()){
			    push(R2);
			}
			else{
			    tape.clearExecutedFlag(IP);
			}
			adjustIP(1);
		    }
		    break;
		    
		case PSH3:{
			if (!isFull()){
			    push(R3);
			}
			else{
			    tape.clearExecutedFlag(IP);
			}
			adjustIP(1);
		    }
		    break;
		    
		case PSH4:{
			if (!isFull()){
			    push(R4);
			}
			else{
			    tape.clearExecutedFlag(IP);
			}
			adjustIP(1);
		    }
		    break;
		    
		case POP1:{
			if (!isEmpty()){
			    R1 = pop();
			}
			else{
			    tape.clearExecutedFlag(IP);
			}
			adjustIP(1);
		    }
		    break;
		    
		case POP2:{
			if (!isEmpty()){
			    R2 = pop();
			}
			else{
			    tape.clearExecutedFlag(IP);
			}
			adjustIP(1);
		    }
		    break;
		    
		case POP3:{
			if (!isEmpty()){
			    R3 = pop();
			}
			else{
			    tape.clearExecutedFlag(IP);
			}
			adjustIP(1);
		    }
		    break;
		    
		case POP4:{
			if (!isEmpty()){
			    R4 = pop();
			}
			else{
			    tape.clearExecutedFlag(IP);
			}
			adjustIP(1);
		    }
		    break;
		    
		case CLLF:{
			if (!isFull()){
			    int ret_addr = (IP + 2) % tape.getSize();
			    int jmpf_pos = IP;				//store the pos. of the JMPF instrutapeion
			    adjustIP(1);					//step to the operand
			    short op = tape.read(IP);
			    if (tape.contains(op)){
				do{						//searching for the operand
				    adjustIP(1);
				}while (tape.fetchInst(IP) != op);
				push(ret_addr);
			    }else{						//otherwise clear the flag for JMPF
				tape.clearExecutedFlag(jmpf_pos);
				adjustIP(1);
			    }
			}
			else{						//otherwise clear the flag for JMPF
			    tape.clearExecutedFlag(IP);
			    adjustIP(2);
			}
			
		    }
		    break;
		    
		case CLLB:{
			if (!isFull()){
			    int ret_addr = (IP + 2) % tape.getSize();
			    int jmpb_pos = IP;				//store the pos. of the JMPF instrutapeion
			    adjustIP(1);					//step to the operand
			    short op = tape.read(IP);
			    if (tape.contains(op)){
				adjustIP(-1);
				do{						//searching for the operand
				    adjustIP(-1);
				}while (tape.fetchInst(IP) != op);
				push(ret_addr);
			    }else{						//otherwise clear the flag for JMPF
				tape.clearExecutedFlag(jmpb_pos);
				adjustIP(1);
			    }
			}
			else{						//otherwise clear the flag for JMPF
			    tape.clearExecutedFlag(IP);
			    adjustIP(2);
			}
		    }
		    break;
		    
		case RETN:{
			if (!isEmpty()){
			    IP = pop();
			}
			else{
			    tape.clearExecutedFlag(IP);
			    adjustIP(1);
			}
		    }
		    break;
		    
		case SCF1:{
			int scf_pos = IP;
			adjustIP(1);
			short op = tape.read(IP);
			if (tape.contains(op)){
			    int pos = IP + 1 % tape.getSize();
			    do {
			        pos = (pos + 1) % tape.getSize();
			    } while (tape.fetchInst(pos) != op);
			    R1 = pos;
			}
			else{
			    tape.clearExecutedFlag(scf_pos);
			    tape.clearExecutedFlag(IP);
			}
			adjustIP(1);
		    }
		    break;
		    
		case SCF2:{
			int scf_pos = IP;
			adjustIP(1);
			short op = tape.read(IP);
			if (tape.contains(op)){
			    int pos = IP + 1 % tape.getSize();
			    do {
			        pos = (pos + 1) % tape.getSize();
			    } while (tape.fetchInst(pos) != op);
			    R2 = pos;
			}
			else{
			    tape.clearExecutedFlag(scf_pos);
			    tape.clearExecutedFlag(IP);
			}
			adjustIP(1);
		    }
		    break;
		    
		case SCF3:{
			int scf_pos = IP;
			adjustIP(1);
			short op = tape.read(IP);
			if (tape.contains(op)){
			    int pos = IP + 1 % tape.getSize();
			    do {
			        pos = (pos + 1) % tape.getSize();
			    } while (tape.fetchInst(pos) != op);
			    R3 = pos;
			}
			else{
			    tape.clearExecutedFlag(scf_pos);
			    tape.clearExecutedFlag(IP);
			}
			adjustIP(1);
		    }
		    break;
		    
		case SCF4:{
			int scf_pos = IP;
			adjustIP(1);
			short op = tape.read(IP);
			if (tape.contains(op)){
			    int pos = IP + 1 % tape.getSize();
			    do {
			        pos = (pos + 1) % tape.getSize();
			    } while (tape.fetchInst(pos) != op);
			    R4 = pos;
			}
			else{
			    tape.clearExecutedFlag(scf_pos);
			    tape.clearExecutedFlag(IP);
			}
			adjustIP(1);
		    }
		    break;
		    
		case SCB1:{
			int scf_pos = IP;
			adjustIP(1);
			short op = tape.read(IP);
			if (tape.contains(op)){
			    int pos = IP - 1;
			    if (pos < 0){ pos = tape.getSize() - 1;}
			    do {
			        pos--;
				if (pos < 0){ pos = tape.getSize() - 1;}
			    } while (tape.fetchInst(pos) != op);
			    R1 = pos;
			}
			else{
			    tape.clearExecutedFlag(scf_pos);
			    tape.clearExecutedFlag(IP);
			}
			adjustIP(1);
		    }
		    break;
		    
		case SCB2:{
			int scf_pos = IP;
			adjustIP(1);
			short op = tape.read(IP);
			if (tape.contains(op)){
			    int pos = IP - 1;
			    if (pos < 0){ pos = tape.getSize() - 1;}
			    do {
			        pos--;
				if (pos < 0){ pos = tape.getSize() - 1;}
			    } while (tape.fetchInst(pos) != op);
			    R2 = pos;
			}
			else{
			    tape.clearExecutedFlag(scf_pos);
			    tape.clearExecutedFlag(IP);
			}
			adjustIP(1);
		    }
		    break;
		    
		case SCB3:{
			int scf_pos = IP;
			adjustIP(1);
			short op = tape.read(IP);
			if (tape.contains(op)){
			    int pos = IP - 1;
			    if (pos < 0){ pos = tape.getSize() - 1;}
			    do {
			        pos--;
				if (pos < 0){ pos = tape.getSize() - 1;}
			    } while (tape.fetchInst(pos) != op);
			    R3 = pos;
			}
			else{
			    tape.clearExecutedFlag(scf_pos);
			    tape.clearExecutedFlag(IP);
			}
			adjustIP(1);
		    }
		    break;
		    
		case SCB4:{
			int scf_pos = IP;
			adjustIP(1);
			short op = tape.read(IP);
			if (tape.contains(op)){
			    int pos = IP - 1;
			    if (pos < 0){ pos = tape.getSize() - 1;}
			    do {
			        pos--;
				if (pos < 0){ pos = tape.getSize() - 1;}
			    } while (tape.fetchInst(pos) != op);
			    R4 = pos;
			}
			else{
			    tape.clearExecutedFlag(scf_pos);
			    tape.clearExecutedFlag(IP);
			}
			adjustIP(1);
		    }
		    break;
		    
		case LBL0:
		case LBL1:
		case LBL2:
		case LBL3:
		case LBL4:
		case LBL5:
		case LBL6:
		case LBL7:
		case LBL8:
		case LBL9:{
			adjustIP(1);
		    }
		    break;
		    
		case NOOP:{
			tape.clearExecutedFlag(IP);
			adjustIP(1);
		    }
		    break;
		    
	    }
	    counter++;
	}catch (Exception e) {
	    Log.error("Error in PrimordialSoupVM's main cycle: " + e.getMessage());
	    e.printStackTrace();
	    System.exit(-1);
	}
	
	
    }
    
    public void execute(int n) {
	for (int i = 0; i < n; i++){
	    execute();
	}
    }
    
    
    
    
    
    /**
     *  The instruction pointer-register is incremented.
     *  The codetape is circular.
     *  @param value the amount of incrementation
     */
    void adjustIP(int value){
	while (value < 0){
	    value = value + tape.getSize();
	}
	IP = (IP + value) % tape.getSize();
    }
    
    /**
     * Just sets directly the IP.
     */
    void setIP(int value) {
	while (value < 0){
	    value = value + tape.getSize();
	}
	IP = value % tape.getSize();
    }
    
    protected void push(int val){
	stack[SP] = val;
	SP++;
    }
    
    protected int pop(){
	
	return stack[--SP];
    }
    
    protected boolean isFull(){ return SP == STACK_SIZE; }
    protected boolean isEmpty() { return SP == 0;}
    
    public Genome getGenome(){ return tape.getGenome(); }
    public int getGenomeSize() { return tape.getSize(); }
    
    public int getEffectiveLength() { return tape.calculateEffectiveLength(); }
    
    public String getState(){
	StringBuffer sb = new StringBuffer();
	sb.append("IP: " + IP + " R1: " + R1 + " R2: " + R2 + " R3: " + R3 + " R4: " + R4);
	if (!isEmpty()){
	sb.append(" STACK [");
	for (int i = 0; i < SP; i++){
	    sb.append(stack[i] + ",");
	}
	sb.append("\b]");
	}
	else{
	    sb.append(" stack is empty");
	}
	sb.append("\n\nINST: " +  InstructionSet.getInstance().getInstructionMnemonic(tape.read(IP))) ;
	return sb.toString();
	
    }

    public String getProcessorInformation(){
	return "";
    }
}
