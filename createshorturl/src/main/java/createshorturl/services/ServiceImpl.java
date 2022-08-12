package createshorturl.services;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

import createshorturl.events.Events;
import createshorturl.generator.IDGenerator;
import lombok.val;
import shorturls.exceptions.InvalidArgumentException;
import shorturls.model.URLItem;
import urlutils.idvalidator.BaseURL;
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

	
	private URLItem newURLItem(URL longURL,Long expirationTime){
		val id = idGenerator.generateUniqueID();
		return new URLItem(id,longURL,LocalDateTime.now(),expirationTime);
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
	public URL createShortURL(URL longURL, Long time) throws InvalidArgumentException {
		if ((validSeconds(time)) && (validURL(longURL))){
			val seconds = getEpochSeconds(time);
			val item = newURLItem(longURL,seconds);
			events.send(item);
			return getShortURL(item.getShortPath());
		}else{
			throw new InvalidArgumentException("Invalid arguments");
		}
	}

	private Long getEpochSeconds(Long time) {
		return (System.currentTimeMillis()/1000)+time;
	}


	private boolean validSeconds(Long time) {
		return (time > 0);
	}

	private URL getShortURL(String id) {
		try {
			return new URL(baseURL.toURL()+id);
		} catch (MalformedURLException e) {
			throw new ServiceException("An internal error has ocurred creating the short URL",e);
		}
	}

	public static void main(String[] args) {
		Long n =  100L/3;
		System.out.println(n);
	}
	
}
