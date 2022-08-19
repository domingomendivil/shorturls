package createshorturl.services;

import java.net.URL;

import shorturls.exceptions.InvalidArgumentException;

/**
 * The Service interface, which is responsible for the logic and communicates between
 * the data layer and the API. 
 * 
 *
 */
public interface Service {
	

	/**
	 * Creates a short URL associated with a long (original) URL.
	 * @param longURL the original URL for which a short URL must be created. 
	 * An InvalidArgumentException is thrown in case the longURL is not valid.
	 * For a URL to be valid, it must have the corresponding format.
	 * It must be an http or https URL. 
	 * Examples of invalid URLs are the following:
	 * ftp://me.li/../XKJDFD
	 * file:///tmp/dir
	 * @return the short URL created
	 * @throws InvalidArgumentException the exception thrown in case the URL is not valid
	 */
	public URL createShortURL(URL longURL) throws InvalidArgumentException ;
	
	/**
	 * Creates a short URL associated with a long (original) URL. The short URL will 
	 * exist and can be used for the period in seconds which is also passed as an input
	 * argument. After that period, the short URL is deleted automatically and cannot be
	 * used. 
	 * @param longURL the original URL for which a short URL must be created
	 * @param seconds The seconds that the short URL will exist.
	 * @return
	 * @throws InvalidArgumentException
	 */
	public URL createShortURL(URL longURL,Long seconds) throws InvalidArgumentException;
	


}
