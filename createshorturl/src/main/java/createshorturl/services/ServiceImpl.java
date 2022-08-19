package createshorturl.services;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

import createshorturl.events.Events;
import createshorturl.generator.IDGenerator;
import lombok.val;
import shorturls.exceptions.InvalidArgumentException;
import shorturls.exceptions.ShortURLRuntimeException;
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
	 * Events interface to send the creation of the short URL
	 */
	private final Events events;
	
	/*
	 * IDGenerator generates codes for new short URLs.
	 * The code is what comes after the base url. 
	 * E.g. in http://me.li/XKJFXd the code is "XKJFXd"
	 */
	private final IDGenerator idGenerator;
	
	
	/*
	 * The base URL is the first part of the short url
	 * E.g. in http://me.li/XKJFXd the baseURL is "http://me.li"
	 */
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
			throw new InvalidArgumentException("Invalid expiration time");
		}

	}

	private boolean validURL(URL longURL) {
		val protocol = longURL.getProtocol();
		return (protocol.equalsIgnoreCase("http") || (protocol.equalsIgnoreCase("https")));
	}


	@Override
	public URL createShortURL(URL longURL, Long seconds) throws InvalidArgumentException {
		if ((validSeconds(seconds)) && (validURL(longURL))){
			val epochSeconds = getEpochSeconds(seconds);
			val item = newURLItem(longURL,epochSeconds);
			events.send(item);
			return getShortURL(item.getShortPath());
		}else{
			throw new InvalidArgumentException("Invalid arguments");
		}
	}

	private Long getEpochSeconds(Long seconds) {
		return (System.currentTimeMillis()/1000)+seconds;
	}


	private boolean validSeconds(Long time) {
		return (time > 0);
	}

	private URL getShortURL(String id) {
		try {
			return new URL(baseURL.toURL()+id);
		} catch (MalformedURLException e) {
			throw new ShortURLRuntimeException("An internal error has ocurred creating the short URL",e);
		}
	}

}
