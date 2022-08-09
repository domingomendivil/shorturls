package createshorturl.services;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

import createshorturl.events.Events;
import createshorturl.generator.IDGenerator;
import lombok.val;
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

	
	private URLItem newURLItem(URL longURL){
		val id = idGenerator.generateUniqueID();
		URLItem urlItem = new URLItem();
		urlItem.setShortPath(id);
		urlItem.setCreationDate(LocalDateTime.now());
		urlItem.setLongURL(longURL);
		return urlItem;
	}

	@Override
	public URL createShortURL(URL longURL) throws InvalidArgumentsException {
		if (validURL(longURL)){
			val item = newURLItem(longURL);
			events.send(item);
			return getShortURL(item.getShortPath());
		}else{
			throw new InvalidArgumentsException();
		}

	}

	private boolean validURL(URL longURL) {
		var protocol = longURL.getProtocol();
		return (protocol.equalsIgnoreCase("http") || (protocol.equalsIgnoreCase("https")));
	}


	@Override
	public URL createShortURL(URL longURL, Long hours) throws InvalidArgumentsException {
		if ((validHours(hours)) && (validURL(longURL))){
			val item = newURLItem(longURL);
			item.setExpirationHours(hours);
			events.send(item);
			return getShortURL(item.getShortPath());
		}else{
			throw new InvalidArgumentsException();
		}
	}

	private boolean validHours(Long hours) {
		return ((hours > 0) && (hours<10001));
	}

	private URL getShortURL(String id) {
		try {
			return new URL(baseURL.toURL()+id);
		} catch (MalformedURLException e) {
			throw new ServiceException("An internal error has ocurred creating the short URL",e);
		}
	}
	
}
