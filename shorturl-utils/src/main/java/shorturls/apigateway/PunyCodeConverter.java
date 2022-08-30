package shorturls.apigateway;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringJoiner;

import lombok.val;

public class PunyCodeConverter {
	
	private static final String POINT =".";
	private static final String SLASH ="/";
	
	/**
	 * Gets the original URL and transform it to a punycode one.
	 * This transforms special UNICODE characters to ASCII equivalent.
	 * This transformation is necessary for browsers, because
	 * HTTP headers are only allowed to have ASCII characters.
	 * There may be some browsers that interpret unicode characters, 
	 * but for have 100% guarantees, it's better to convert them
	 * to ASCII ones.
	 * Examples of URLs with special characters are:
	 * http://www.peñarol.org.
	 * This URL will be converted to http://www.xn--pearol-xwa.org/ 
	 * @param url The original URL to punycode.
	 * @return the punycoded URL
	 * @throws MalformedURLException
	 */
	public static String toPunycode(String url) throws MalformedURLException {
		URL u = new URL(url);
		val punicoded =getPunicoded(u.getHost(),POINT);
		val pathPunicoded = getPunicoded(u.getPath(),SLASH);
		return u.getProtocol()+"://"+punicoded+pathPunicoded;
	}
	
	private static String getPunicoded(String url,String separator) {
		String[] labels = url.split("\\"+separator);
		val joiner = new StringJoiner(separator);
		for (int i=0;i<labels.length;i++) {
			String str= java.net.IDN.toASCII(labels[i]);
			joiner.add(str);
		}
		return joiner.toString();
	}

}
