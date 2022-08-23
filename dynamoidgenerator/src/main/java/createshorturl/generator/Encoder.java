package createshorturl.generator;

import java.math.BigInteger;

/**
 * Incoder of big integers to a given
 * encoding.
 * 
 *
 */
interface Encoder {
	/**
	 * Returns the encoded big integer
	 * @param value the big integer to encode
	 * @return the encoded big integer
	 */
    public String encode(BigInteger value);
}
