package geturl.services;

import java.net.URL;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.events.Events;

import lombok.val;
import shorturls.dao.Query;
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

	private final Query query;

	private final IdValidator idValidator;

	private final Events events;

	public ServiceImpl(Query query, IdValidator idValidator, Events events) {
		this.query = query;
		this.idValidator = idValidator;
		this.events = events;
	}

	@Override
	public Optional<URL> getLongURL(URL shortURL, Map<String, String> headers) throws InvalidArgumentsException {
		try {
			val code = idValidator.getCode(shortURL);
			return getURL(code, headers);
		} catch (ValidationException e) {
			throw new InvalidArgumentsException(e);
		}

	}

	@Override
	public Optional<URL> getURL(String shortPathId, Map<String, String> headers) throws InvalidArgumentsException {
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
			try {
				val json = new ObjectMapper().writeValueAsString(headers);
				events.send(shortPath, json);
			} catch (JsonProcessingException e) {
				// this exception is not thrown
			}
		}
	}

}
