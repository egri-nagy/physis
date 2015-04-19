/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: HandyMethods.java,v 1.2 2003/02/19 15:30:31 sirna Exp $
 * $Revision: 1.2 $
 * $Date: 2003/02/19 15:30:31 $
 */
package physis.util;

public class HandyMethods{
    /**
     * Putting zeros in front of an integer. Useful for generating filenames. 
     */
    public static String getLongNumberString(long num, int length){
	String ns = Long.toString(num);
	StringBuilder sb = new StringBuilder(length+2);
	
	for (int i = 0; i < length - ns.length(); i++){
	    sb.append('0');
	}
	sb.append(ns);
	return sb.toString();
    }
    
    /**
     * Pretty printing of an int array.
     */
    public static String prettyPrintIntArray(int[] array){
	StringBuilder sb = new StringBuilder();
	sb.append("[");
	for (int i = 0; i < array.length; i++){
	    sb.append(array[i] + ",");
	}
	sb.append("\b]");
	return sb.toString();
    }

    /**
     * Pretty printing of a boolean array.
     */
    public static String prettyPrintIntArray(boolean[] array){
	StringBuilder sb = new StringBuilder();
	sb.append("[");
	for (int i = 0; i < array.length; i++){
	    sb.append(array[i] + ",");
	}
	sb.append("\b]");
	return sb.toString();
    }


}
