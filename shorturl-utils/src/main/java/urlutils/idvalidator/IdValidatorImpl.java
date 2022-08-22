package urlutils.idvalidator;

import java.net.URL;

import lombok.val;


public class IdValidatorImpl implements IdValidator {


	private final BaseURL baseURL;
	
	private static final String LOCALHOST="localhost";
	
	private static final String LOCAL_IP="127.0.0.1";

	private final String alphabet;

	private final Integer length;
	
	public IdValidatorImpl(BaseURL baseURL,String alphabet,Integer length) {
		this.baseURL=baseURL;
		this.alphabet=alphabet;
		this.length=length;
	}
	
	@Override
	public boolean isValid(String shortPath) {
		if ((shortPath==null) || (shortPath.equals("") || (shortPath.length()!=length))) {
			return false;
		}
		for (int i=0;i<shortPath.length();i++){
			val index= alphabet.indexOf(shortPath.charAt(i));
			if (index<0){
				return false;
			}
		}
		return true;
	}
	
	public String getCode(URL shortURL) throws ValidationException {
		val path = shortURL.getPath();
		if (!path.equals("")) {
			var i = path.length() - 1;
			val sb = new StringBuilder();
			while (path.charAt(i) != '/') {
				sb.append(path.charAt(i));
				i--;
			}
			val shortCode = sb.reverse().toString();
			var base = shortURL.toString().replace(path, "")+"/";
			if (base.contains("localhost")) {
				base = base.replace(LOCALHOST,LOCAL_IP);
			}
			String temp= baseURL.toURL();
			if (temp.contains("localhost")) {
				temp = temp.replace(LOCALHOST,LOCAL_IP);
			}
			if (base.equals(temp))  {
				if (isValid(shortCode)) {
					return shortCode;
				}
			}
		}
		throw new ValidationException();
	}


}
