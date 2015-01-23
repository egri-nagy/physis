package physis.core.random;

/**
 * This is essentially the guts of java.util.Random as of jdk1.3.
 *
 * @author Brian O. Bush
 * @version 1.0
 *
 * Thu 31-May-2001 00:13 bush
 *
 * from the header in java.util.Random:
 *
 * An instance of this class is used to generate a stream of
 * pseudorandom numbers. The class uses a 48-bit seed, which is
 * modified using a linear congruential formula. (See Donald Knuth,
 * <i>The Art of Computer Programming, Volume 2</i>, Section 3.2.1.)
 * <p>
 * If two instances of <code>Random</code> are created with the same
 * seed, and the same sequence of method calls is made for each, they
 * will generate and return identical sequences of numbers. In order to
 * guarantee this property, particular algorithms are specified for the
 * class <tt>Random</tt>. Java implementations must use all the algorithms
 * shown here for the class <tt>Random</tt>, for the sake of absolute
 * portability of Java code. However, subclasses of class <tt>Random</tt>
 * are permitted to use other algorithms, so long as they adhere to the
 * general contracts for all the methods.
 *
 */
public class JavaUtilRandom extends Randomness {
    private long seed;
    
    private final static long multiplier = 0x5DEECE66DL;
    private final static long addend = 0xBL;
    private final static long mask = (1L << 48) - 1;
    
    public JavaUtilRandom(long seed) {
	super(seed);
	setSeed(seed);
    }
    
    public final void setSeed(long seed) {
	this.seed = (seed ^ multiplier) & mask;
    }
    
    /**
     * Generates the next pseudorandom number. Subclass should
     * override this, as this is used by all other methods.<p>
     * The general contract of <tt>next</tt> is that it returns an
     * <tt>int</tt> value and if the argument bits is between <tt>1</tt>
     * and <tt>32</tt> (inclusive), then that many low-order bits of the
     * returned value will be (approximately) independently chosen bit
     * values, each of which is (approximately) equally likely to be
     * <tt>0</tt> or <tt>1</tt>. The method <tt>next</tt> is implemented
     * by class <tt>Random</tt> as follows:
     * <blockquote><pre>
     * synchronized protected int next(int bits) {
     *       seed = (seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
     *       return (int)(seed >>> (48 - bits));
     * }</pre></blockquote>
     * This is a linear congruential pseudorandom number generator, as
     * defined by D. H. Lehmer and described by Donald E. Knuth in <i>The
     * Art of Computer Programming,</i> Volume 2: <i>Seminumerical
     * Algorithms</i>, section 3.2.1.
     *
     * @param   bits random bits
     * @return  the next pseudorandom value from this random number generator's sequence.
     * @since   JDK1.1
     */
    public final int next(int bits) {
	long nextseed = (seed * multiplier + addend) & mask;
	seed = nextseed;
	return (int)(nextseed >>> (48 - bits));
    }
    
    public static void main(String [] args) {
	JavaUtilRandom r = new JavaUtilRandom(4357);
	for (int j=0;j<1000;j++) {
	    // first, convert the int from signed to "unsigned"
	    long l = (long)r.nextInt();
	    if (l < 0 ) l += 4294967296L;  // max int value
	    String s = String.valueOf(l);
	    while(s.length() < 10) s = " " + s;  // buffer
	    System.out.print(s + " ");
	    if (j%5==4) System.out.println();
	}
    }
    
}
