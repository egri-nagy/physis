/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Stack.java,v 1.7 2003/05/08 12:24:54 sirna Exp $
 * $Revision: 1.7 $
 * $Date: 2003/05/08 12:24:54 $
 */
package arche;

/**
 * Stack as a structural element for processors. There is no error checking, you can read more
 * than you actually have put. Of course if the stack is empty you'll get meaningless zeros. If 
 * the stack is full and you would like to put one value then yoour action will be cancelled.
 */
public class Stack extends Storage {
    private byte size;
    private short[] stack;
    /** The stack pointer. Points the last put element. */
    private byte sp;
    
    public Stack(int size_){
	size = (byte) size_;
	stack = new short[size];
	sp = -1;
    }

    public int getSize(){return size;}
    
    public short read(){
	//if it's empty the caller gets a zero
	if (sp < 0){ return 0;}
	return stack[sp--];
    }
    /** Sets the content to a new value. */
    public void write(short newvalue){
	if (sp < size-1){ stack[++sp] = newvalue;}
    }
    
    public void clear(){
        sp = -1;
	for (int i = 0; i < stack.length; i++){
	    stack[i] = 0;
	}
    }

    public String getStructure(){ return "S" + size; }
    
    public String toString(){
      StringBuilder sb = new StringBuilder();
      sb.append("stackof" + size + "[");
      if (sp < 0){
        sb.append("empty]");
	return sb.toString();
      }
      
      for (int i = sp; i >=0;i--){
          sb.append(stack[i] + ",");
      }
      sb.append("\b]");
      return sb.toString();
    }
}
