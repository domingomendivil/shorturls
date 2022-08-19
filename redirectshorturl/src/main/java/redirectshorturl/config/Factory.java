package redirectshorturl.config;

import geturl.config.ServiceFactory;
import geturl.services.Service;
import lombok.Getter;
import redirectshorturl.apigateway.RedirectShortURL;
import shorturls.config.ShortURLProperties;
import shorturls.config.ShortURLPropertiesImpl;

/**
 * Class Factory for creating a ShortURL class instance.
 *
 */
public class Factory {
	
	private Factory() {
		//this class cannot be instantiated
	}
	
	/*
	 * A Lombok Getter is used to facilitate the instantiation of the class
	 */
	@Getter(lazy=true) private static final RedirectShortURL redirectShortURL = init();

	private static RedirectShortURL init() {
		Service service = ServiceFactory.getInstance();
		return new RedirectShortURL(service,new ShortURLPropertiesImpl());
	}

}
