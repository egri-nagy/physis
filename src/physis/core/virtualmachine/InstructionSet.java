/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: InstructionSet.java,v 1.14 2003/01/16 12:48:18 sirna Exp $
 * $Revision: 1.14 $
 * $Date: 2003/01/16 12:48:18 $
 */
package physis.core.virtualmachine;

import java.io.*;
import java.util.*;

import physis.core.Configuration;
import physis.core.random.Randomness;
import physis.core.PHYSIS;
import physis.log.Log;
/**
 * This class contains the instructons of an instructionset which belongs to one or more virtual machines.
 *    <br>
 *    The persistent storage of the instructions is a simple textfile which has the following format:
 *    <br>
 *    [instruction_set_name]<br>
 *    TYPE [type_name]   [type_code]<br>
 *    [instruction_name]  [instruction_code]<br>
 *    ...<br>
 *
 *    TYPE ....
 *    <br>
 *  <br>
 *    CRC<br>
 *    1. ensure access to instruction_names by codes and vice-versa  - Instruction<br>
 * @author sirna
 * @date 1999.12.8.
 */
public class InstructionSet implements Serializable {
    /**
     * The constructor creates the hashtables, parses the instruction_file given by the instruction_file_name parameter, creates the Instruction objects and fills the  hashtables.
     */

    /**
     *  The nop instruction should be in all instructionsets. And only its code is allowed to and should be less than zero.
     */
    private final static Instruction BLANK_INSTRUCTION = new Instruction(0, (short) -1, "blank", 0);
    private static InstructionSet instance;

    public static InstructionSet getInstance(){
        if (instance == null){
	    instance = new InstructionSet(Configuration.getPhysisHome() + Configuration.getProperty("instructionset"));
        }
        return instance;
    }

    public static InstructionSet getInstance(String instructionset_file){
        if (instance == null){
	    instance = new InstructionSet(instructionset_file);
        }
        return instance;
    }

    private InstructionSet(String instruction_file_name) {
        Log.status("Reading instructionset from file: " + instruction_file_name);
        try{

	    BufferedReader br = new BufferedReader(
		new FileReader(instruction_file_name));

	    //the first line of the file is the name of instructionset
	    String s = br.readLine().trim();
	    name = s;

	    Integer actual_type_code = null;

	    size = 0;
	    type_names = new Hashtable();
	    instructions_by_names = new Hashtable();
	    instructions_by_codes = new Hashtable();


	    String type_name = null;
	    Instruction instruction = null;
	    Short instruction_code = null;
	    String instruction_name = null;
	    int number_of_operands = 0;

	    //the blank should be in
	    instructions_by_names.put(BLANK_INSTRUCTION.getMnemonic(), BLANK_INSTRUCTION);
            instructions_by_codes.put(new Short(BLANK_INSTRUCTION.getInstructionCode()), BLANK_INSTRUCTION);
	    while ( (s = br.readLine()) != null){
		s = s.trim();

		//if it is a blank line or a remark (lines beginning with #)
		if ( (s.equals("")) || (s.startsWith("#") )) continue;

		StringTokenizer st = new StringTokenizer(s);
		String first_token = (String)st.nextToken();


		//if it is a type-def. line
		if (first_token.equals("TYPE")){
		    type_name = (String)st.nextToken();
		    actual_type_code = new Integer(Integer.parseInt((String)st.nextToken()));
		    type_names.put(actual_type_code,type_name);
		}
		else{
		    instruction_name = first_token;
		    instruction_code = new Short(Short.parseShort((String)st.nextToken()));
		    number_of_operands = Integer.parseInt(st.nextToken());

		    instruction = new Instruction(actual_type_code.intValue(), instruction_code.shortValue(), instruction_name, number_of_operands);

		    instructions_by_names.put(instruction_name,instruction);
		    instructions_by_codes.put(instruction_code,instruction);

		    size++;
		}
	    }


	    br.close();



	    //creating the instructioncodearray for random instructioncode-generation - ugly but who cares when initializing
	    Object[] tmp = instructions_by_codes.values().toArray();
	    instcodeArray = new Instruction[tmp.length];
	    for (int i = 0; i < tmp.length; i++ ) instcodeArray[i] = (Instruction)tmp[i];

	    Log.status(Log.getTimeStamp() + " InstructionSet: " + name + " with " + size
			   + " instructions is succesfully created.");

        }catch (Exception e){
	    Log.error("Cannot parse instructionset file: " + instruction_file_name);
	    e.printStackTrace();
	    Log.error(e.getMessage());
        }
    }
    /**
     * Gives the name of the corresponding integer type code. It is useful only for people, for debugging...
     */
    public String getTypeName(int instruction_type_code) {
        return (String)(type_names.get(new Integer(instruction_type_code)));
    }
    /**
     * Gives the thin InstructionCode object according to the given instruction_code.
     */
    public Instruction getInstruction(short instruction_code) {
        return (Instruction)(instructions_by_codes.get(new Short(instruction_code)));
    }

    public Instruction getInstructionByName(String instname){
        return (Instruction) (instructions_by_names.get(instname));
    }

    public Instruction getInstructionByCode(short code){
        return (Instruction) (instructions_by_codes.get(new Short(code)));
    }


    /**
     * Gives the instruction's name (mnemonic) corresponding to the given instruction_code if it's available,
     * otherwise returns the string representation of the instruction code as a short number.
     */
    public String getInstructionMnemonic(short instruction_code) {
	Short code = new Short(instruction_code);
	Instruction instr = (Instruction)instructions_by_codes.get(code);
	if (instr != null){
	    return instr.getMnemonic();
	}
	else{
	    return code.toString();  
	}
    }

    public Instruction getBlankInstrunction() { return BLANK_INSTRUCTION;}

    /**
     * This method is used when mutating the codetape.
     */
    public short getInstructionCodeRandomly(){
        return instcodeArray[random.nextInt(instcodeArray.length)].inst_code;
    }
    public String getName() {
        return name;
    }
    public int getSize() {
        return size;
    }
    /**
     * This hashtable stores the typenames of the instruction categories. The keys are integers.
     */
    private Hashtable type_names;
    /**
     * The keys are Integers, and the values are Instructions. With help of this hashtable it's easy to get all the information about instructions.
     */
    private Hashtable instructions_by_codes;
    /**
     * The keys are Strings (the names of the instructions), and the values are Instructions. With help of this hashtable it's easy to get all the information about instructions.
     */
    private Hashtable instructions_by_names;
    /**
     * The identifier name of the instructionset.
     */
    private String name;
    private Instruction[] instcodeArray;
    private int size;
    //if the environment is not up then instructionset is instatiated outside from physis
    private Randomness random = PHYSIS.environment.getRandomness();
}
