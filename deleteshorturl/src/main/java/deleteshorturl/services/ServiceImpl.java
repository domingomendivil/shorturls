package deleteshorturl.services;

import java.net.URL;

import shorturls.dao.Deleter;
import shorturls.exceptions.InvalidArgumentException;
import urlutils.idvalidator.IdValidator;
import urlutils.idvalidator.ValidationException;

/**
 * Implementation of the Service Layer. It validates the inputs and communicates
 * with data layer and the unique id generator.
 *
 */
final public class ServiceImpl implements Service {

	/*
	 * Deleter interface for deleting the short URL
	 */
	private final Deleter deleter;


	/*
	 * Validator responsible for validating the correct format of the short URL 
	 */
	private final IdValidator idValidator;

	
	public ServiceImpl(Deleter deleter, IdValidator idValidator) {
		this.idValidator = idValidator;
		this.deleter=deleter;
	}

	@Override
	public boolean deleteURL(URL shortURL) throws InvalidArgumentException {
		String shortPath;
		try {
			shortPath = idValidator.getCode(shortURL);
			return deleter.deleteById(shortPath);
		} catch (ValidationException e) {
			throw new InvalidArgumentException(e);
		}
		
	}

	@Override
	public boolean deleteURL(String shortPath) throws InvalidArgumentException {
		if (idValidator.isValid(shortPath)){
			return deleter.deleteById(shortPath);
		}else{
			throw new InvalidArgumentException("Invalid URL");
		}
	
	}

}
