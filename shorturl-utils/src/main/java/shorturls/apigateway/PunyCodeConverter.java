package shorturls.apigateway;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringJoiner;

import lombok.val;

/**
 * Utility class for encoding URLs to punycode.
 * 
 */
public class PunyCodeConverter {
	
	private static final String POINT =".";
	private static final String SLASH ="/";
	private static final String POINT_DOUBLE_SLASH ="://";
	
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
	 * This URL will be converted to http://www.xn--pearol-xwa.org/.
	 * In case that the URL to convert is invalid, it throws a MalformedURLException
	 * @param url The original URL to punycode.
	 * @return the punycoded URL
	 * @throws MalformedURLException the exception thrown in case that the URL to convert is invalid
	 */
	public static String toPunycode(String url) throws MalformedURLException {
		val u = new URL(url);
		val punicoded =getPunicoded(u.getHost(),POINT);
		val pathPunicoded = getPunicoded(u.getPath(),SLASH);
		val sb = new StringBuffer();
		sb.append(u.getProtocol()).append(POINT_DOUBLE_SLASH).append(punicoded).append(pathPunicoded);
		return sb.toString();
	}
	
	/**
	 * Method to convert to punycode each part of the URL.
	 * Basically the two parts to convert are the domain (base part)
	 * and the rest of the path.
	 * E.g. in http://www.peñarol.org/a/index.html
	 * the domain part is www.peñarol.org
	 * @param string The string to convert to punycode
	 * @param separator The separator part, in case of the base part is the point "."
	 * @return the punycode converted string
	 */
	private static String getPunicoded(String string,String separator) {
		val labels = string.split("\\"+separator);
		val joiner = new StringJoiner(separator);
		for (int i=0;i<labels.length;i++) {
			val str= java.net.IDN.toASCII(labels[i]);
			joiner.add(str);
		}
		return joiner.toString();
	}

}
