package urlutils.idvalidator;

import java.net.URL;

/**
 * Validator interface for validating codes of short URLs
 * and getting thoses codes from already given short URLs
 *
 */
public interface IdValidator {

	/**
	 * Tells if a given code of a short URL is valid in terms
	 * of format and length
	 * @param shortPath the code of the short URL
	 * @return if a code is valid
	 */
	public boolean isValid(String shortPath);
	
	/**
	 * Gets the code from a given short URL. 
	 * E.g. for the short URL http://me.li/bsak1A
	 * it returns "bsak1A"
	 * @param shortURL the short URL to know the code from
	 * @return  the code of the short URL
	 * @throws ValidationException the exception thrown in case 
	 * that the short URL is not valid. E.g. trying to get the
	 * code from "http://aa" will throw a ValidationException
	 */
	public String getCode(URL shortURL) throws ValidationException;

}
