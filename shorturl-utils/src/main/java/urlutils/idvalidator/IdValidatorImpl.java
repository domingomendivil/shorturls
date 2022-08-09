package urlutils.idvalidator;

import java.net.URL;


public class IdValidatorImpl implements IdValidator {


	private final BaseURL baseURL;
	
	private static final String LOCALHOST="localhost";
	
	private static final String LOCAL_IP="127.0.0.1";

	private final String alphabet;
	
	public IdValidatorImpl(BaseURL baseURL,String alphabet) {
		this.baseURL=baseURL;
		this.alphabet=alphabet;
	}
	
	@Override
	public boolean isValid(String shortPath) {
		if ((shortPath==null) || (shortPath.equals(""))) {
			return false;
		}
		for (int i=0;i<shortPath.length();i++){
			int index= alphabet.indexOf(shortPath.charAt(i));
			if (index<0){
				return false;
			}
		}
		return true;
	}
	
	public String getCode(URL shortURL) throws ValidationException {
		String path = shortURL.getPath();
		if (!path.equals("")) {
			int i = path.length() - 1;
			StringBuilder sb = new StringBuilder();
			while (path.charAt(i) != '/') {
				sb.append(path.charAt(i));
				i--;
			}
			String shortCode = sb.reverse().toString();
			String base = shortURL.toString().replace(path, "")+"/";
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
