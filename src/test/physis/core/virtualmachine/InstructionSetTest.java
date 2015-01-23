/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: InstructionSetTest.java,v 1.4 2000/10/24 15:07:44 sirna Exp $
 * $Revision: 1.4 $
 * $Date: 2000/10/24 15:07:44 $
 */
package test.physis.core.virtualmachine;

import physis.core.*;
import physis.core.virtualmachine.*;

public class InstructionSetTest{
        //Of course just for testing...
    public static void main(String[] args) {
	Configuration.init(args[0]);
        InstructionSet is = InstructionSet.getInstance();
        System.out.println(is.getSize());
        System.out.println(is.getInstructionMnemonic( (short) 31));
	System.out.println(is.getInstructionByName("zero"));
    }

}
