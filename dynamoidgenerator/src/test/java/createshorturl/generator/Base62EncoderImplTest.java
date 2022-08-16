package createshorturl.generator;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test class for Base62EncoderImpl
 * 
 */
public class Base62EncoderImplTest {
	
	@Test
	public void test1() {
		String encode= new Base62EncoderImpl().encode(BigInteger.valueOf(0));
		assertEquals("000000", encode);
	}

	@Test
	public void test2() {
		String encode= new Base62EncoderImpl().encode(BigInteger.valueOf(1000));
		assertEquals("0000g8", encode);
	}

}
