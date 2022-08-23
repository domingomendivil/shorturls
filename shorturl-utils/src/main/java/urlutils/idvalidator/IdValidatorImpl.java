package urlutils.idvalidator;

import java.net.URL;
import java.util.regex.Pattern;

import lombok.val;

/**
 * Validator of the short urls. It validates that the code is in a valid format,
 * and also gets the code, given the full short URL
 *
 */
public class IdValidatorImpl implements IdValidator {

	/*
	 * The base URL part of the short URL. E.g. in case that the url is
	 * http://me.li/aVH8xl the base url will be http://me.li
	 */
	private final BaseURL baseURL;

	/*
	 * Localhost constant
	 */
	private static final String LOCALHOST = "localhost";

	/*
	 * IP of localhost
	 */
	private static final String LOCAL_IP = "127.0.0.1";


	private final Pattern pattern;

	/*
	 * Length of the codes of the short URLs. Suggested length is greater than 6
	 */
	private final Integer length;

	private static final String SLASH ="/";

	private static final String BLANK = "";

	public IdValidatorImpl(BaseURL baseURL, String alphabet, Integer length) {
		this.baseURL = baseURL;
		this.pattern = Pattern.compile(alphabet);
		this.length = length;
	}

	@Override
	public boolean isValid(final String shortPath) {
		if ((shortPath == null) || (shortPath.equals("") || (shortPath.length() != length))) {
			return false;
		}
		return pattern.matcher(shortPath).find();
	}

	/**
	 * Gets the code from the short URL, validating it. 
	 * In case the code is invalid it throws a ValidationException
	 */
	public String getCode(final URL shortURL) throws ValidationException {
		val path = shortURL.getPath();
		if (!path.equals(BLANK)) {
			val shortCode = path.substring(path.lastIndexOf(SLASH) + 1);
			val oldBase = shortURL.toString().replace(path, BLANK) + SLASH;
			val base = oldBase.replace(LOCALHOST, LOCAL_IP);
			val temp = baseURL.toURL().replace(LOCALHOST, LOCAL_IP);
			if (base.equals(temp)) {
				if (isValid(shortCode)) {
					return shortCode;
				}
			}
		}
		throw new ValidationException();
	}

}
