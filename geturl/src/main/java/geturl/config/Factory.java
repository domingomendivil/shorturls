package geturl.config;

import geturl.apigateway.GetLongURL;
import geturl.apigateway.GetURL;
import geturl.services.Service;
import lombok.Getter;
import lombok.val;

/**
 * Factory class responsible for instantiating a GetLongURL and GetURL classes
 */
public class Factory {
	
	private Factory() {
		//this class cannot be instantiated
	}
	

	/*
	 * A Lombok Getter annotations is used to facilitate the instantiation
	 */
	@Getter(lazy=true) private static final GetLongURL longUrl = initLongURL();
	
	/*
	 * A Lombok Getter annotations is used to facilitate the instantiation
	 */
	@Getter(lazy=true) private static final GetURL url = initURL();

	
	/*
	 * Method used for creating a new GetLongURL class
	 */
	private static GetLongURL initLongURL() {
			val service = ServiceFactory.getInstance();
			return new GetLongURL(service);
	}
	
	/*
	 * Method used for creating a new GetURL class
	 */
	private static GetURL initURL() {
			val service = ServiceFactory.getInstance();
			return new GetURL(service);
	}



	
	

}
