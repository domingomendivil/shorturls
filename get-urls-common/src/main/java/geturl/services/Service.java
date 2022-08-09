package geturl.services;

import java.net.URL;
import java.util.Map;
import java.util.Optional;

/**
 * The Service interface, which is responsible for the logic and communicates between
 * the data layer and the API. 
 * 
 *
 */
public interface Service {
	

		/**
	 * Gets the original URL associated with a short URL.  In case that the 
	 * URL is not found, it return an empty optional. The short URL must be valid, 
	 * otherwise an InvalidArgumentsException is thrown. For a short URL to be valid, it must
	 * have the corresponding format. Must start with the corresponding base URL, 
	 * that is, the protocol and the domain must be the same. 
	 * If the short URLs are like the following: http://me.li/XKJDFD
	 * you cannot delete a url like http://otherdomain.com/XKJDFD, because the domain 
	 * is not valid. Other examples of invalid short URLs are the following:
	 * http://me.li/../XKJDFD
	 * ftp://me.li/../XKJDFD
	 * file:///tmp/dir
	 * @param shortURL the Short URL for which the original URL must be found
	 * @return the original URL
	 * @throws InvalidArgumentsException the exception thrown in case that the short URL
	 * is invalid.
	 */
	public Optional<URL> getLongURL(URL shortURL,Map<String,String> headers) throws InvalidArgumentsException ;
	
	public Optional<URL> getURL(String shortPath,Map<String,String> headers) throws InvalidArgumentsException ;
		
}
