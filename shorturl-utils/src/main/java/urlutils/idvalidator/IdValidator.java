package urlutils.idvalidator;

import java.net.URL;

public interface IdValidator {

	boolean isValid(String shortPath);
	
	public String getCode(URL shortURL) throws ValidationException;

}
