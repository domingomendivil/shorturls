package deleteshorturl.services;

import java.net.URL;

/**
 * The Service interface, which is responsible for the logic and communicates between
 * the data layer and the API. 
 * 
 *
 */
public interface Service {
	

	/**
	 * Deletes an already generated short URL. In case that the URL is doesn't exist, 
	 * this method does nothing. 
	 * In case the short URL is not valid, it
	 * throws and InvalidArgumentException. For a short URL to be valid, it must
	 * have the corresponding format. Must start with the corresponding base URL, 
	 * that is, the protocol and the domain must be the same. 
	 * If the short URLs are like the following: http://me.li/XKJDFD
	 * you cannot delete a url like http://otherdomain.com/XKJDFD, because the domain 
	 * is not valid. Other examples of invalid short URLs are the following:
	 * http://me.li/../XKJDFD
	 * ftp://me.li/../XKJDFD
	 * file:///tmp/dir
	 * @param shortURL the short URL to delete
	 * @throws InvalidArgumentsException
	 */
	public boolean deleteURL(URL shortURL) throws InvalidArgumentsException;
	

}
