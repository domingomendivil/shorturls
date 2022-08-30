package shorturls.apigateway;

import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * 
 * @author d0178
 *
 */
public class PunyCodeConverterTest {
	
	@Test
	public void test1() throws MalformedURLException {
		String result = PunyCodeConverter.toPunycode("http://www.peñarol.org");
		assertEquals("http://www.xn--pearol-xwa.org", result);
	}
	
	@Test
	public void test2() throws MalformedURLException {
		String result = PunyCodeConverter.toPunycode("http://www.peñarol.org/a.html");
		assertEquals("http://www.xn--pearol-xwa.org/a.html", result);
	}
	
	@Test
	public void test3() throws MalformedURLException {
		String result = PunyCodeConverter.toPunycode("http://www.peñarol.org/b/a.html");
		assertEquals("http://www.xn--pearol-xwa.org/b/a.html", result);
	}


}
