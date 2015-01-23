/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: MarVM.java,v 1.32 2001/08/09 11:00:43 sirna Exp $
 * $Revision: 1.32 $
 * $Date: 2001/08/09 11:00:43 $
 */
package mar.virtualmachine;

import physis.core.virtualmachine.VirtualMachine;
import physis.core.virtualmachine.PhysisVirtualMachine;
import physis.core.virtualmachine.GeneticCodeTape;
import physis.core.virtualmachine.InstructionSet;
import physis.core.event.ProliferationEvent;
import physis.core.event.InteractionEvent;
import physis.core.PHYSIS;
import physis.core.genotype.Genome;
import physis.log.Log;

/**
 * Simple stack machine with one stack and one register.
 */
public class MarVM extends PhysisVirtualMachine{
    
    /** Size of the internal stack. (hardcoded 16, because it's the attribute of the architecture */
    public static final int STACK_SIZE = 16;
    protected int[] stack = new int[STACK_SIZE];
    public static final int EMPTY = 0;
    public static final int FULL = STACK_SIZE;
    /** stack_pointer, it points to the next available element, also equals with stacksize */
    protected int SP = EMPTY;
    
    
    private int register;
    /** instruction pointer */
    private int IP;
    
    //INSTRUCTIONS
    final static short label_f = 10;
    final static short label_b = 11;
    final static short if_less = 20;
    final static short if_n_equ = 21;
    final static short jump_f = 22;
    final static short jump_b = 23;
    final static short shift_r = 30;
    final static short shift_l = 31;
    final static short inc = 32;
    final static short dec = 33;
    final static short add = 40;
    final static short sub = 41;
    final static short nand = 42;
    final static short swap = 43;
    final static short copy = 50;
    final static short allocate = 51;
    final static short divide = 52;
    final static short get = 60;
    final static short put = 61;
    final static short count_f = 62;
    final static short count_b = 63;
    final static short top2reg = 70;
    final static short push = 71;
    final static short pop = 72;
    final static short zero = 73;
    final static short rotate_f = 80;
    final static short rotate_b = 81;
    final static short rotate_rand = 82;
    final static short nop = 90;
    
    /** FOR OPTIMIZATION: the divide operation is like a transaction so the VMs can share the ProliferationEvent instance. */
    private static ProliferationEvent prolevnt = new ProliferationEvent(null, null);
    /** FOR OPTIMIZATION: the I/O operation are like transactions so the VMs can share the InteractionEvent instance. */
    private static InteractionEvent interactevnt = new InteractionEvent(null);
    
    /**
     * Executes the next instruction.
     */
    public void execute(){
        try{
	    //calls the method corresponding the instructioncode
	    switch ( tape.read(IP) ){
		case label_f : incrementIP(1); break;
		case label_b : incrementIP(1); break;
		case blank     : tape.clearExecutedFlag(IP);incrementIP(1); break;
		case nop : tape.clearExecutedFlag(IP);incrementIP(1); break;
		case if_less : ifless(); break;
		case if_n_equ : if_n_equ(); break;
		case jump_f : jump_f(); break;
		case jump_b : jump_b(); break;
		case shift_r : shift_r(); break;
		case shift_l : shift_l(); break;
		case inc : inc(); break;
		case dec : dec(); break;
		case add: add(); break;
		case sub : sub(); break;
		case nand : nand(); break;
		case swap : swap(); break;
		case copy : copy(); break;
		case allocate : allocate(); break;
		case divide : divide(); break;
		case get : get(); break;
		case put : put(); break;
		case count_f: count_f(); break;
		case count_b: count_b(); break;
		case top2reg : top2reg(); break;
		case push : push(); break;
		case pop: pop(); break;
		case zero    : zero(); break;
		case rotate_f : rotate_f(); break;
		case rotate_b : rotate_b(); break;
		case rotate_rand: rotate_rand(); break;
	    }
	    counter++;
        }catch (Exception e) {
	    Log.error("Error in MarVM's main cycle: SP = " + SP  + e.getMessage());
	    e.printStackTrace();
	    System.exit(-1);
        }
    }
    
    public MarVM(){
    }
    
    public void execute(int n) {
        for (int i = 0; i < n; i++){
	    execute();
        }
    }
        
    
    /**
     * Brings the machine into the base-state. It's used when vivivying the organism.
     * <br>
     * Stack is  empty, register contains 0.
     */
    public void reset(){
        restart();
        gestation_time = GESTATION_TIME_INVALID;
    }
    
    /** Brings the machine into base-state but doesn't change the gestation time.
     *  It's used after succesfull proliferation.
     */
    public void restart(){
        register = 0;
        stackEmpty();
        setIP(0);
        counter = 0;
    }
    
    public Genome getGenome(){ return tape.getGenome(); }
    public int getGenomeSize() { return tape.getSize(); }
    
    public String getState(){
	StringBuffer sb = new StringBuffer();
        sb.append("IP: " + IP + " REGISTER: " + register + " STACK: [");
	for (int i = 0; i < SP; i++){
	    sb.append(" " + stack[i] + " ");
	}
	sb.append("]");
	sb.append("\n\nINST: " +  InstructionSet.getInstance().getInstructionMnemonic(tape.read(IP))) ;
	return sb.toString();
	
    }
    
    public String getProcessorInformation(){
	return "";
    }
    
    /**
     *  The instruction pointer-register is incremented.
     * The codetape is circular.
     *  @param value the amount of incrementation
     */
    void incrementIP(int value){
        IP = (IP + value) % tape.getSize();
    }
    
    void setIP(int value) { IP = value; }
    
    //THE INSTRUCTIONS
    /**
     * If the top of the stack is less than the registercontent executes the next instruction,
     * otherwise skips it. Empty stack is not equal.
     */
    void ifless(){
        if ((!stackEmpty()) && (stackTop() < register)){
	    incrementIP(1);
        }
        else{
	    incrementIP(2);
        }
    }
    
    /**
     * If the top of the stack is less than the registercontent executes the next instruction,
     * otherwise skips it. Empty stack is not equal.
     */
    void if_n_equ(){
        if (!stackEmpty() && (stackTop() != register)){
	    incrementIP(1);
        }
        else{
	    incrementIP(2);
        }
    }
    
    void jump_f(){
        //If it doesn't contain then searching for it means infinite-loop.
        if (!tape.contains(label_f)){
	    tape.clearExecutedFlag(IP);
	    incrementIP(1);
	    return;
        }
        
        int tape_size = tape.getSize();
        
        int i = (IP + 1) % tape_size;
        int numberof_jumps = 1;
        
        //counting the number of jump insrtructions
        while ( (jump_f == tape.fetchInst(i) )
	       || ( jump_b == tape.fetchInst(i)) ){
	    numberof_jumps++;
	    i = (i + 1) % tape_size;
        }
        
        //finding the nth forward_label
        for (int j = 0; j < numberof_jumps; j++){
	    while ( tape.fetchInst(i) != label_f ) {
		i = (i + 1) % tape_size;
	    }
        }
        
        setIP(i);
    }
    
    void jump_b(){
        if (!tape.contains(label_b)){
	    tape.clearExecutedFlag(IP);
	    incrementIP(1);
	    return;
        }
        
        int tape_size = tape.getSize();
        
        int i = (IP + 1) % tape_size;
        int numberof_jumps = 1;
        
        //counting the number of jump insrtructions
        while ( (jump_f == tape.fetchInst(i) ) || ( jump_b == tape.fetchInst(i)) ){
	    numberof_jumps++;
	    i = (i + 1) % tape_size;
        }
        
        i = IP;
        
        //finding the nth backward_label
        for (int j = 0; j < numberof_jumps; j++){
	    while ( tape.fetchInst(i) != label_b ) {
		i--;
		if ( i < 0 ) {
		    i = tape_size - 1;
		}
	    }
        }
        
        setIP(i);
        
    }
    
    //one-argument math
    void shift_r(){
        if (!stackEmpty()){
	    stackPush(stackPop() >> 1);
	}
	else {
	    tape.clearExecutedFlag(IP);
	}
	
        incrementIP(1);
    }
    
    void shift_l(){
        if (!stackEmpty()){
	    stackPush(stackPop() << 1);
	}
        else {
	    tape.clearExecutedFlag(IP);
	}
	incrementIP(1);
    }
    
    void inc(){
        if (!stackEmpty()){
	    stackPush(stackPop() + 1);
	}
        else {
	    tape.clearExecutedFlag(IP);
	}
        incrementIP(1);
    }
    
    void dec(){
        if (!stackEmpty()) {
	    stackPush(stackPop() - 1);
	}
        else{
	    tape.clearExecutedFlag(IP);
	}
        incrementIP(1);
    }
    
    //double argument_math
    void add() {
        if (SP >= 2){
	    stackPush(stackPop() + stackPop());
        }
	else{
	    tape.clearExecutedFlag(IP);
	}
        incrementIP(1);
    }
    
    void sub() {
        if (SP >= 2){
	    stackPush((-stackPop()) + stackPop());
	}
	else{
	    tape.clearExecutedFlag(IP);
	}
        incrementIP(1);
    }
    
    void nand() {
        if (SP >= 2){
	    stackPush( ~(stackPop() & stackPop()) );
	}
	else{
	    tape.clearExecutedFlag(IP);
	}
        incrementIP(1);
    }
    
    void swap() {
        if (SP >= 2){
	    int tmp1 = stackPop();
	    int tmp2 = stackPop();
	    stackPush(tmp1);
	    stackPush(tmp2);
        }else {
	    tape.clearExecutedFlag(IP);
	}
        incrementIP(1);
    }
    
    protected void copy(){
        if ( (stackEmpty())){
	    tape.clearExecutedFlag(IP);
	    incrementIP(1);
	    return;
        }
        
	if (!tape.copy(stackTop(),stackTop() + register)){
	    tape.clearExecutedFlag(IP);
	}
        incrementIP(1);
    }
    
    protected void allocate(){
	if (tape.isAllocationPossible(register)){
	    tape.allocate(register);
	}
	else {
	    tape.clearExecutedFlag(IP);
	}
	incrementIP(1);
    }
    
    protected void divide(){
        try{
	    if (!tape.isProliferationPossible()) {
		tape.clearExecutedFlag(IP);
		
		incrementIP(1);
		return;
	    }
	    gestation_time = counter;
	    prolevnt.reFill(tape.divide(),bearer);
	    PHYSIS.environment.proliferationPerformed(prolevnt);
        } catch(Exception e){
	    Log.error(e);
        }
        
    }
    
    //pop to environment
    void put(){
        try{
	    
	    if (stackEmpty()){
		tape.clearExecutedFlag(IP);
		incrementIP(1);
		return;
	    }
	    //should be optimized!
	    if (!bearer.getMetabolism().isOutputFull()) {
		bearer.getMetabolism().putOutputValue(stackPop());
		interactevnt.reFill(bearer);
		PHYSIS.environment.interactionOccured(interactevnt);
	    }
	    else {
		bearer.getMetabolism().clear();
	    }
        } catch(Exception e){
	    Log.error(e);
        }
	incrementIP(1);
        
    }
    
    void get(){
        try{
	    if (!bearer.getMetabolism().isInputFull() && (!stackFull())){
		stackPush(PHYSIS.environment.getInputData());
		bearer.getMetabolism().putInputValue(stackTop());
		interactevnt.reFill(bearer);
		PHYSIS.environment.interactionOccured(interactevnt);
	    }
	    else{
		tape.clearExecutedFlag(IP);
	    }
	    incrementIP(1);
        } catch(Exception e){
	    Log.error(e);
        }
        
    }
    
    void count_f(){
        if (!tape.contains(label_f)){
	    tape.clearExecutedFlag(IP);
	    incrementIP(1);
	    return;
        }
        
        int tape_size = tape.getSize();
        
        //this variable counts the intstructions
        int i = 1;
        int numberof_counts = 1;
        
        
        //counting the number of jump insrtructions
        while ( (count_f == tape.fetchInst((IP + i) % tape_size) )
	       || ( count_b == tape.fetchInst((IP + i) % tape_size ) ) ){
	    numberof_counts++;
	    i++;
        }
        
        //finding the nth forward_label
        int k  = (IP + i) % tape_size;
        for (int j = 0; j < numberof_counts; j++){
	    while ( tape.fetchInst(k) != label_f ) {
		k = (k + 1) % tape_size;
		i++;
	    }
        }
        
        register = i + 1 ;  //because it's inclusive.. :)
        incrementIP(1);
    }
    
    void count_b(){
        if (!tape.contains(label_b)){
	    tape.clearExecutedFlag(IP);
	    incrementIP(1);
	    return;
        }
        
        int tape_size = tape.getSize();
        
        int numberof_counts = 1;
        
        //counting the number of jump insrtructions
        while ( (count_f == tape.fetchInst( (IP + numberof_counts) % tape_size) )
	       || ( count_b == tape.fetchInst((IP + numberof_counts) % tape_size) ) ){
	    numberof_counts++;
        }
        
        
        
        //finding the nth forward_label
        
        int i = 0;
        int k = IP;
        for (int j = 0; j < numberof_counts; j++){
	    while ( tape.fetchInst(k) != label_b ) {
		k--;
		if ( k < 0 ) {
		    k = tape_size - 1;
		}
		i++;
	    }
        }
        register = i;
        incrementIP(1);
        
        
    }
    
    void top2reg(){
        if (!stackEmpty()){
	    register = stackTop();
	}
	else {
	    tape.clearExecutedFlag(IP);
	}
	
	incrementIP(1);
    }
    
    //pushing register
    void push(){
        if (!stackFull()) {
	    stackPush(register);
	}
        incrementIP(1);
    }
    //poping into register
    void pop(){
        if (!stackEmpty()){
	    register = stackPop();
	}
	else {
	    tape.clearExecutedFlag(IP);
	}
	incrementIP(1);
    }
    
    void rotate_f(){
        PHYSIS.environment.rotateForward(bearer);
        incrementIP(1);
    }
    void rotate_b(){
        incrementIP(1);
    }
    
    void rotate_rand(){
        incrementIP(1);
    }
    
    void zero(){
        if (!stackFull()) {
	    stackPush(0);
	}
        incrementIP(1);
    }
    
    void stackPush(int val){ stack[SP++] = val; }
    int stackPop(){  return stack[--SP];}
    int stackTop() { return stack[SP - 1]; }
    boolean stackEmpty() { return SP == EMPTY; }
    boolean stackFull() { return SP == FULL; }
    void stackClear() { SP = EMPTY; }
}
