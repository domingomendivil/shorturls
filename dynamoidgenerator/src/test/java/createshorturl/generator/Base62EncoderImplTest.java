package createshorturl.generator;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

public class Base62EncoderImplTest {
	
	@Test
	public void test1() {
		String encode= new Base62EncoderImpl().encode(BigInteger.valueOf(0));
		Assert.assertEquals("000000", encode);
	}

	@Test
	public void test2() {
		String encode= new Base62EncoderImpl().encode(BigInteger.valueOf(1000));
		Assert.assertEquals("0000g8", encode);
	}

}
