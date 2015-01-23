package physis.core.random;

import java.io.*;
import java.util.*;

/**
 * <h3>Mersenne Twister</h3>
 * <p>This work is based on version MT199937 (99/10/29)
 * of the Mersenne Twister algorithm found at
 * <a href="http://www.math.keio.ac.jp/matumoto/emt.html">
 * The Mersenne Twister Home Page
 * </a>.
 *
 * <h3>About the Mersenne Twister</h3>
 * <p>This is a Java version of the C-program for MT19937: Integer version.
 * The MT19937 algorithm was created by Makoto Matsumoto and Takuji Nishimura,
 * who ask: "When you use this, send an email to: matumoto@math.keio.ac.jp
 * with an appropriate reference to your work".  Indicate that this
 * is a translation of their algorithm into Java.
 *
 * <p><b>Reference. </b>
 * Makato Matsumoto and Takuji Nishimura,
 * "Mersenne Twister: A 623-Dimensionally Equidistributed Uniform
 * Pseudo-Random Number Generator",
 * <i>ACM Transactions on Modeling and Computer Simulation,</i>
 * Vol. 8, No. 1, January 1998, pp 3--30.
 *
 * <h3>About this Version</h3>
 * This version of the code implements the MT19937 Mersenne Twister
 * algorithm, with the 99/10/29 seeding mechanism.  The original
 * mechanism did not permit 0 as a seed, and odd numbers were not
 * good seed choices.  The new version permits any 32-bit signed integer.
 * This algorithm is identical to the MT19937 integer algorithm; real
 * values conform to Sun's float and double random number generator
 * standards rather than attempting to implement the half-open or
 * full-open MT19937-1 and MT199937-2 algorithms.
 *
 * <p><b>Changes Since V3:</b> Slight modification to the coin flip algorithms
 * ensures that 0.0 and 1.0 probabilities always return false and true.
 *
 * <p>The MersenneTwister code is based on standard MT19937 C/C++
 * code by Takuji Nishimura,
 * with suggestions from Topher Cooper and Marc Rieffel, July 1997.
 * The code was originally translated into Java by Michael Lecuyer,
 * January 1999, and is Copyright (c) 1999 by Michael Lecuyer.
 * The included license is as follows:
 *
 * <blockquote><font size="-1">
 * The basic algorithmic work of this library (appearing in nextInt()
 * and setSeed()) is free software; you can redistribute it and or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later
 * version.
 *
 * <p>This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Library General Public License for more details.
 * You should have received a copy of the GNU Library General
 * Public License along with this library; if not, write to the
 * Free Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307  USA
 * </font></blockquote>
 *
 * <h3>Important Note on Seeds</h3>
 *
 * <p> Just like java.util.Random, this
 * generator accepts a long seed but doesn't use all of it. java.util.Random
 * uses 48 bits.  The Mersenne Twister instead uses 32 bits (int size).
 * So it's best if your seed does not exceed the int range.
 *
 * @author Brian O. Bush
 * @version 1.0
 *
 */

public class MersenneTwister extends Randomness {
    // Period parameters
    private static final int N = 624;
    private static final int M = 397;
    private static final int MATRIX_A = 0x9908b0df;   //    private static final * constant vector a
    private static final int UPPER_MASK = 0x80000000; // most significant w-r bits
    private static final int LOWER_MASK = 0x7fffffff; // least significant r bits
    
    // Tempering parameters
    private static final int TEMPERING_MASK_B = 0x9d2c5680;
    private static final int TEMPERING_MASK_C = 0xefc60000;
    
    // #define TEMPERING_SHIFT_U(y)  (y >>> 11)
    // #define TEMPERING_SHIFT_S(y)  (y << 7)
    // #define TEMPERING_SHIFT_T(y)  (y << 15)
    // #define TEMPERING_SHIFT_L(y)  (y >>> 18)
    
    private int mt[]; // the array for the state vector
    private int mti; // mti==N+1 means mt[N] is not initialized
    private int mag01[];
    
    // a good initial seed (of int size, though stored in a long)
    private static final long DEFAULT_SEED = 4357;
    
    /**
     * Constructor using the default seed.
     */
    public MersenneTwister() {
	this(DEFAULT_SEED);
    }
    
    /**
     * Constructor using a given seed.  Though you pass this seed in
     * as a long, it's best to make sure it's actually an integer.
     */
    public MersenneTwister(long seed) {
	super(seed);
	setSeed(seed);
    }
    
    /**
     * Initalize the pseudo random number generator.  This is the
     * old seed-setting mechanism for the original Mersenne Twister
     * algorithm.  You must not use 0 as your seed, and don't
     * pass in a long that's bigger than an int (Mersenne Twister
     * only uses the first 32 bits for its seed).  Also it's suggested
     * that for you avoid even-numbered seeds in this older
     * seed-generation procedure.
     *
     */
    public void setSeedOld(long seed) {
	mt = new int[N];
	
	// setting initial seeds to mt[N] using
	// the generator Line 25 of Table 1 in
	// [KNUTH 1981, The Art of Computer Programming
	//    Vol. 2 (2nd Ed.), pp102]
	
	// the 0xffffffff is commented out because in Java
	// ints are always 32 bits; hence i & 0xffffffff == i
	mt[0]= ((int)seed); // & 0xffffffff;
	
	for (mti = 1; mti < N; mti++) {
	    mt[mti] = (69069 * mt[mti-1]); //& 0xffffffff;
	}
	
	// mag01[x] = x * MATRIX_A  for x=0,1
	mag01 = new int[2];
	mag01[0] = 0x0;
	mag01[1] = MATRIX_A;
    }
    
    /**
     * Initalize the pseudo random number generator.  Don't
     * pass in a long that's bigger than an int (Mersenne Twister
     * only uses the first 32 bits for its seed).
     *
     */
    public final void setSeed(long seed) {
	
	// seed needs to be casted into an int first for this to work
	int _seed = (int)seed;
	
	mt = new int[N];
	
	for (int i=0;i<N;i++) {
	    mt[i] = _seed & 0xffff0000;
	    _seed = 69069 * _seed + 1;
	    mt[i] |= (_seed & 0xffff0000) >>> 16;
	    _seed = 69069 * _seed + 1;
	}
	
	mti=N;
	// mag01[x] = x * MATRIX_A  for x=0,1
	mag01 = new int[2];
	mag01[0] = 0x0;
	mag01[1] = MATRIX_A;
    }
    
    /**
     * Returns an integer with <i>bits</i> bits filled with a random number.
     * Note: There is no validation or bounding of bits.
     */
    public final int next(int bits) {
	int y;
	
	if (mti >= N) { // generate N words at one time
	    int kk;
	    
	    for (kk = 0; kk < N - M; kk++) {
		y = (mt[kk] & UPPER_MASK) | (mt[kk+1] & LOWER_MASK);
		mt[kk] = mt[kk+M] ^ (y >>> 1) ^ mag01[y & 0x1];
	    }
	    for (; kk < N-1; kk++) {
		y = (mt[kk] & UPPER_MASK) | (mt[kk+1] & LOWER_MASK);
		mt[kk] = mt[kk+(M-N)] ^ (y >>> 1) ^ mag01[y & 0x1];
	    }
	    y = (mt[N-1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
	    mt[N-1] = mt[M-1] ^ (y >>> 1) ^ mag01[y & 0x1];
	    
	    mti = 0;
	}
	
	y = mt[mti++];
	y ^= y >>> 11;                          // TEMPERING_SHIFT_U(y)
	y ^= (y << 7) & TEMPERING_MASK_B;       // TEMPERING_SHIFT_S(y)
	y ^= (y << 15) & TEMPERING_MASK_C;      // TEMPERING_SHIFT_T(y)
	y ^= (y >>> 18);                        // TEMPERING_SHIFT_L(y)
	
	return y >>> (32 - bits);    // hope that's right!
    }
    
    /**
     * Tests the code.
     */
    public static void main(String args[]) {
	int j;
	
	// TEST FOR CORRECTNESS
	// WITH ORIGINAL ALGORITHM
	MersenneTwister r = new MersenneTwister(4357);
	r.setSeedOld(4357);
	System.out.println("Output of MersenneTwister, old style");
	for (j=0;j<1000;j++) {
	    // first, convert the int from signed to "unsigned"
	    long l = (long)r.nextInt();
	    if (l < 0 ) l += 4294967296L;  // max int value
	    String s = String.valueOf(l);
	    while(s.length() < 10) s = " " + s;  // buffer
	    System.out.print(s + " ");
	    if (j%8==7) System.out.println();
	}
	
	// TEST FOR CORRECTNESS WITH
	// NEW VERSION MT19937 1999/10/28
	// COMPARE WITH http://www.math.keio.ac.jp/~nisimura/random/int/mt19937int.out
	r = new MersenneTwister(4357);
	System.out.println("Output of MersenneTwister with new (1999/10/28) seeding mechanism");
	for (j=0;j<1000;j++) {
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
