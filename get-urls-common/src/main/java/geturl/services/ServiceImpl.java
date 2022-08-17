package geturl.services;

import java.net.URL;
import java.util.Map;
import java.util.Optional;

import com.meli.events.Events;

import lombok.val;
import shorturls.dao.Query;
import shorturls.exceptions.InvalidArgumentException;
import shorturls.model.URLItem;
import urlutils.idvalidator.IdValidator;
import urlutils.idvalidator.ValidationException;

/**
 * Implementation of the Service Layer.
 * It validates the inputs and communicates with data layer
 * and the unique id generator.
 *
 */
public class ServiceImpl implements Service {

	/*
	 * Query data layer for getting the URL
	 */
	private final Query query;

	/*
	 * IdValidator validates that the code used in the short URLs.
	 * The code is the part after the base URL. 
	 * E.g. in http://me.li/XKJFx7, the code is XKJFx7
	 * Codes can be invalid if for example invalid characters are used.
	 * E.g. X/JFx7 is an invalid code  
	 */
	private final IdValidator idValidator;

	/*
	 * Events layer where the getURL event is sent
	 */
	private final Events events;
	



	/**
	 * Public constructor where the Query, IdValidator and Events object are injected
	 * @param query
	 * @param idValidator
	 * @param events
	 */
	public ServiceImpl(Query query, IdValidator idValidator, Events events) {
		this.query = query;
		this.idValidator = idValidator;
		this.events = events;
	}

	@Override
	public Optional<URL> getLongURL(URL shortURL, Map<String, String> headers) throws InvalidArgumentException {
		try {
			val code = idValidator.getCode(shortURL);
			return getURL(code, headers);
		} catch (ValidationException e) {
			throw new InvalidArgumentException(e);
		}

	}

	@Override
	public Optional<URL> getURL(String shortPathId, Map<String, String> headers) throws InvalidArgumentException {
		val urlItem = query.getById(shortPathId);
		return urlItem.map(item -> {
			send(item, headers);
			return item.getLongURL();
		});
	}

	
	private void send(URLItem urlItem, Map<String, String> headers) {
		if (headers != null) {
			val shortPath = urlItem.getShortPath();
			headers.put("shortURL", urlItem.getShortPath());
			headers.put("longURL", urlItem.getLongURL().toString());
			headers.put("creationDate", urlItem.getCreationDate().toString());
			events.send(shortPath, headers);
		}
	}

}
