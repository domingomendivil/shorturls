package deleteshorturl.services;

import java.net.URL;

import shorturls.dao.Deleter;
import urlutils.idvalidator.IdValidator;
import urlutils.idvalidator.ValidationException;

/**
 * Implementation of the Service Layer. It validates the inputs and communicates
 * with data layer and the unique id generator.
 *
 */
public class ServiceImpl implements Service {

	private final Deleter deleter;


	private final IdValidator idValidator;

	public ServiceImpl(Deleter deleter, IdValidator idValidator) {
		this.idValidator = idValidator;
		this.deleter=deleter;
	}

	@Override
	public boolean deleteURL(URL shortURL) throws InvalidArgumentsException {
		String shortPath;
		try {
			shortPath = idValidator.getCode(shortURL);
			return deleter.deleteById(shortPath);
		} catch (ValidationException e) {
			throw new InvalidArgumentsException(e);
		}
		
	}

}
