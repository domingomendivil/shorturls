package createshorturl.services;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

import createshorturl.events.Events;
import createshorturl.generator.IDGenerator;
import lombok.val;
import shorturls.exceptions.InvalidArgumentException;
import shorturls.model.URLItem;
/**
 * Implementation of the Service Layer. 
 * It validates the inputs and communicates with data layer
 * and the unique id generator. 
 *
 */
public class ServiceImpl implements Service {

	/*
	 * 
	 */
	private final Events events;
	private final IDGenerator idGenerator;
	private final BaseURL baseURL;
	

	public ServiceImpl(Events events,IDGenerator idGenerator,BaseURL baseURL){
		this.events=events;
		this.idGenerator=idGenerator;
		this.baseURL=baseURL;
	}

	
	private URLItem newURLItem(URL longURL,Long expirationHours){
		val id = idGenerator.generateUniqueID();
		return new URLItem(id,longURL,LocalDateTime.now(),expirationHours);
	}

	@Override
	public URL createShortURL(URL longURL) throws InvalidArgumentException {
		if (validURL(longURL)){
			val item = newURLItem(longURL,null);
			events.send(item);
			return getShortURL(item.getShortPath());
		}else{
			throw new InvalidArgumentException("Invalid expiration hours (must be between 1 and 10000)");
		}

	}

	private boolean validURL(URL longURL) {
		val protocol = longURL.getProtocol();
		return (protocol.equalsIgnoreCase("http") || (protocol.equalsIgnoreCase("https")));
	}


	@Override
	public URL createShortURL(URL longURL, Long hours) throws InvalidArgumentException {
		if ((validHours(hours)) && (validURL(longURL))){
			val item = newURLItem(longURL,hours);
			events.send(item);
			return getShortURL(item.getShortPath());
		}else{
			throw new InvalidArgumentException("Expiration hours must be between  invalid");
		}
	}

	private boolean validHours(Long hours) {
		return ((hours >= 1) && (hours<=10000));
	}

	private URL getShortURL(String id) {
		try {
			return new URL(baseURL.toURL()+id);
		} catch (MalformedURLException e) {
			throw new ServiceException("An internal error has ocurred creating the short URL",e);
		}
	}
	
}
