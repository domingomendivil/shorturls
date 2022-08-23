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

	/*
	 * Alphabet used in generating the codes of the short URLs
	 */
	private final String alphabet;

	private final Pattern pattern;

	/*
	 * Length of the codes of the short URLs. Suggested length is greater than 6
	 */
	private final Integer length;

	public IdValidatorImpl(BaseURL baseURL, String alphabet, Integer length) {
		this.baseURL = baseURL;
		this.alphabet = alphabet;
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

	public String getCode(final URL shortURL) throws ValidationException {
		val path = shortURL.getPath();
		if (!path.equals("")) {
			var i = path.length() - 1;
			val sb = new StringBuilder();
			while (path.charAt(i) != '/') {
				sb.append(path.charAt(i));
				i--;
			}
			val shortCode = sb.reverse().toString();
			var base = shortURL.toString().replace(path, "") + "/";
			if (base.contains("localhost")) {
				base = base.replace(LOCALHOST, LOCAL_IP);
			}
			String temp = baseURL.toURL();
			if (temp.contains("localhost")) {
				temp = temp.replace(LOCALHOST, LOCAL_IP);
			}
			if (base.equals(temp)) {
				if (isValid(shortCode)) {
					return shortCode;
				}
			}
		}
		throw new ValidationException();
	}

}
