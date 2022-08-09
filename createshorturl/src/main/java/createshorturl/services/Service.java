package createshorturl.services;

import java.net.URL;

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
	 * An InvalidArgumentsException is thrown in case the longURL is not valid.
	 * For a URL to be valid, it must have the corresponding format.
	 * It must be an http or https URL. 
	 * Examples of invalid URLs are the following:
	 * ftp://me.li/../XKJDFD
	 * file:///tmp/dir
	 * @return the short URL created
	 * @throws InvalidArgumentsException the exception thrown in case the URL is not valid
	 */
	public URL createShortURL(URL longURL) throws InvalidArgumentsException ;
	
	/**
	 * Creates a short URL associated with a long (original) URL. The short URL will 
	 * exist and can be used for the period in hours which is also passed as an input
	 * argument. After that period, the short URL is deleted automatically and cannot be
	 * used. 
	 * @param longURL the original URL for which a short URL must be created
	 * @param hours The hours that the short URL will exist.
	 * @return
	 * @throws InvalidArgumentsException
	 */
	public URL createShortURL(URL longURL,Long hours) throws InvalidArgumentsException;
	


}
