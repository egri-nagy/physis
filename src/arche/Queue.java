/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Queue.java,v 1.5 2003/05/08 12:24:54 sirna Exp $
 * $Revision: 1.5 $
 * $Date: 2003/05/08 12:24:54 $
 */

package arche;

/**
 * Queue as a structural element for processors. There is no error checking, you can read more
 * than you actually have put. Of course the values will be periodical.
 */
public class Queue extends Storage {
    private byte size;
    private short[] queue;
    /** The pointers. P1 for the first put, and P2 for the last put. */
    private byte p1, p2;
    
    public Queue(int size_){
	size = (byte) size_;
	queue = new short[size];
	p1 = p2 = 0;
    }

    public int getSize(){return size;}

    public String getStructure(){ return "Q" + size; }
    
    public short read(){
	short tmp = queue[p2];
	p2 = (byte)((p2 + 1) % size);
	return tmp;
    }
    /** Sets the content to a new value. */
    public void write(short newvalue){
	p1 = (byte)((p1 + 1) % size);
	queue[p1] = newvalue;
    }
    
    public void clear(){
        p1 = p2 = 0;
	for (int i = 0; i < queue.length; i++){
	    queue[i] = 0;
	}
    }

}
