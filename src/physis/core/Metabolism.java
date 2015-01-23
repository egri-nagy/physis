/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Metabolism.java,v 1.5 2003/02/18 16:37:21 sirna Exp $
 * $Revision: 1.5 $
 * $Date: 2003/02/18 16:37:21 $
 */
package physis.core;

import java.util.*;

/**
 * It tracks the organism's interaction with the environment.
 *  <br>
 *  CRC<br>
 *  1. tracking the inputs and outputs
 *  <br>
 * Hint: it's actually an I/O buffer. The implementation should handle two queues. Their sizes are defined by the biggest tasks and can be acquired from the Configuration class.
 */
public class Metabolism {
    private int[] inputbuffer;
    private int[] outputbuffer;
    /** the container array for returning the input values. (for better performance)*/
    private int[] inputs;
    /** the container array for returning the output values. (for better performance)*/
    private int[] outputs;
    
    /** The bufferpointers point to the last element. */
    private int iptr = -1;
    private int optr = -1;
    
    public Metabolism(int buffer_size){
        inputbuffer = new int[buffer_size];
        outputbuffer = new int[buffer_size];
	inputs = new int[buffer_size + 1];
	outputs = new int[buffer_size + 1];
    }
    /**
     * Gets the inputvalues in chronological order. The first element is the number of valid elements.
     * WARNING! The returned array is always the same.
     */
    public int[] getInputs() {
        inputs[0] = iptr+1;
        System.arraycopy(inputbuffer,0,inputs,1,iptr+1);
        return inputs;
    }
    
    /**
     * Gets the outputvalues in chronological order. The first element is the number of valid elements.
     * WARNING! The returned array is always the same.
     */
    public int[] getOutputs() {
        outputs[0] = optr+1;
        System.arraycopy(outputbuffer,0,outputs,1,optr+1);
        return outputs;
    }
    
    
    /**
     * Put a value into the input queue.
     */
    public void putInputValue(int val) {
        inputbuffer[++iptr] = val;
    }
    /**
     * Put a value into the output queue.
     */
    public void putOutputValue(int val) {
        outputbuffer[++optr] = val;
    }
    
    /**
     *  Returns the number of inputs.
     */
    public int getInputSize() { return iptr + 1; }
    
    /**
     * Returns the number of outputs.
     */
    public int getOutputSize() { return optr + 1; }
    
    /**
     * Returns the size of the buffers.
     */
    public int getBufferSize() { return inputbuffer.length; }
    
    /**
     *  Clears the buffers.
     */
    public void clear(){
        iptr = optr = -1;
    }
    
    public boolean isInputFull(){
        return iptr == inputbuffer.length - 1 ;
    }
    
    public boolean isOutputFull(){
        return optr == outputbuffer.length - 1;
    }

    public String toString(){
	StringBuffer sb = new StringBuffer();
	int[] tmp = getInputs();
	sb.append("Inputs:");
	for (int i = 0; i < tmp[0]; i++){
	    sb.append(tmp[i+1] + ",");	    
	}
	sb.append("\b");
	tmp = getOutputs();
	sb.append(" Outputs:");
	for (int i = 0; i < tmp[0]; i++){
	    sb.append(tmp[i+1] + ",");	    
	}
	sb.append("\b\n");
	return sb.toString();
    }
    
}
