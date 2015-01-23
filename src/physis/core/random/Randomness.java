package physis.core.random;

/**
 * class Randomness - a very plain boring name for basically
 * a random number generator abstraction layer. Which I use
 * for all of my evolutionary computation work.
 *
 * The past and the future are entirely contained in the present.
 * -Ivar Ekeland, Mathematics and the Unexpected. 1988
 *
 * @author Brian O. Bush
 * @version 1.0
 *
 * Wed 30-May-2001 23:34 bush
 *
 * {Ghost Machine Industries}
 *
 */
public abstract class Randomness {
    
    public Randomness(long seed) {
	System.out.println("<RNG Class=" + getClass().getName() +
			       "; seed=" + seed + ">");
    }
    
    /**
     * Returns a 32 bit uniformly distributed random number in the closed interval
     * <tt>[Integer.MIN_VALUE,Integer.MAX_VALUE]</tt>
     * (including <tt>Integer.MIN_VALUE</tt> and <tt>Integer.MAX_VALUE</tt>).
     */
    public final int nextInt() {
	return nextInt(Integer.MAX_VALUE);
    }
    
    public final int nextInt(int n) {
	if (n <= 0) {
	    throw new IllegalArgumentException("n must be >= 0");
	}
	
	if ((n & -n) == n) {
	    return (int)((n * (long)next(31)) >> 31);
	}
	
	int bits, val;
	do {
	    bits = next(31);
	    val = bits % n;
	} while(bits - val + (n-1) < 0);
	return val;
    }
    
    /**
     * To extend this class, all you need to do is implement this method!
     */
    public abstract int next(int bits);
    
    /**
     * Returns a 64 bit uniformly distributed random number
     * in the closed interval <tt>[Long.MIN_VALUE,Long.MAX_VALUE]</tt>
     * (including <tt>Long.MIN_VALUE</tt> and <tt>Long.MAX_VALUE</tt>).
     */
    public final long nextLong() {
	// concatenate two 32-bit strings into one 64-bit string
	return (next(32) << 32) | (next(32));
    }
    
    public final double nextDouble() {
	return (((long)next(26) << 27) + next(27))
	    / (double)(1L << 53);
    }
    
    public final float nextFloat() {
	return next(24) / ((float)(1 << 24));
    }
    
    /** A bug fix for all versions of the JDK.  The JDK appears to
     use all four bytes in an integer as independent byte values!
     Totally wrong. */
    public final void nextBytes(byte[] bytes) {
	for (int x=0;x<bytes.length;x++) {
	    bytes[x] = (byte)next(8);
	}
    }
    
    public final byte nextByte() {
	return (byte)(next(8));
    }
    
    public final boolean nextBoolean() {
	return next(1) != 0;
    }
    
    /**
     * This generates a coin flip with a probability <tt>probability</tt>
     * of returning true, else returning false. <tt>probability</tt> must
     * be between 0.0 and 1.0, inclusive.
     */
    public final boolean nextBoolean (double probability) {
	if (probability < 0.0 || probability > 1.0)
	    throw new IllegalArgumentException ("probability must be between 0.0 and 1.0 inclusive.");
	if (probability==0.0) return false;		// fix half-open issues
	else if (probability==1.0) return true;	// fix half-open issues
	return nextDouble() < probability;
    }
    
    /**
     * Returns a 32 bit uniformly distributed random
     * number in the open unit interval <code>(0.0,1.0)</code>
     * (excluding 0.0 and 1.0).
     */
    public final double raw() {
	int val;
	do { // accept anything but zero
	    val = nextInt(); // in [Integer.MIN_VALUE,Integer.MAX_VALUE]-interval
	} while (val == 0);
	
	// now transform to (0.0,1.0)-interval
	// 2.3283064365386963E-10 == 1.0 / Math.pow(2,32)
	return (double) (val & 0xFFFFFFFFL) * 2.3283064365386963E-10;
	/*
	 nextInt == Integer.MAX_VALUE   --> 0.49999999976716936
	 nextInt == Integer.MIN_VALUE   --> 0.5
	 nextInt == Integer.MAX_VALUE-1 --> 0.4999999995343387
	 nextInt == Integer.MIN_VALUE+1 --> 0.5000000002328306
	 nextInt == 1                   --> 2.3283064365386963E-10
	 nextInt == -1                  --> 0.9999999997671694
	 nextInt == 2                   --> 4.6566128730773926E-10
	 nextInt == -2                  --> 0.9999999995343387
	 */
    }
    
}
