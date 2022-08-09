package redirectshorturl.config;

import geturl.config.ServiceFactory;
import geturl.services.Service;
import lombok.Getter;
import redirectshorturl.apigateway.RedirectShortURL;

public class Factory {
	
	private Factory() {
		//this class cannot be instantiated
	}
	
	@Getter(lazy=true) private static final RedirectShortURL redirectShortURL = init();

	private static RedirectShortURL init() {
		Service service = ServiceFactory.getInstance();
		return new RedirectShortURL(service);
	}

}
