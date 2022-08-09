package createshorturl.generator;

import java.math.BigInteger;

class Base62EncoderImpl implements Base62Encoder{

	private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	@Override
	public String encode(BigInteger value) {
	    StringBuilder sb = new StringBuilder();
	    BigInteger length = BigInteger.valueOf(62);	
	    BigInteger zero = BigInteger.valueOf(0);	

	    while (value.compareTo(zero)>0) {
	    	int mod = value.mod(length).intValue();
	        sb.append(BASE62.charAt(mod));
	        value = value.divide(length);
	    }
	    while (sb.length() < 6) {
	        sb.append(0);
	    }
	    return sb.reverse().toString();
	}    
	
	public String encodeUUID(String uuid) {
		BigInteger bigInt = new BigInteger(uuid, 16);  
		return encode(bigInt);
		
	}

	
}