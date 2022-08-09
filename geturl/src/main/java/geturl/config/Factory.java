package geturl.config;

import geturl.apigateway.GetLongURL;
import geturl.apigateway.GetURL;
import geturl.services.Service;
import lombok.Getter;

public class Factory {
	
	private Factory() {
		//this class cannot be instantiated
	}
	
	
	@Getter(lazy=true) private static final GetLongURL longUrl = initLongURL();
	
	
	@Getter(lazy=true) private static final GetURL url = initURL();

	private static GetLongURL initLongURL() {
			Service service = ServiceFactory.getInstance();
			return new GetLongURL(service);
	}
	
	private static GetURL initURL() {
			Service service = ServiceFactory.getInstance();
			return new GetURL(service);
	}



	
	

}
