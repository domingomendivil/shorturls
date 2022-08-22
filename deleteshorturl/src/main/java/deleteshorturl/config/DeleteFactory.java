package deleteshorturl.config;

import static shorturls.constants.Constants.BASE_URL;
import static shorturls.constants.Constants.CACHE_ENABLED;
import static shorturls.constants.Constants.CACHE_FACTORY;
import static shorturls.constants.Constants.DELETER_FACTORY;
import static shorturls.constants.Constants.RANDOM_ALPHABET;
import static shorturls.constants.Constants.RANDOM_LENGTH;

import com.meli.factory.Factory;

import deleteshorturl.apigateway.DeleteShortPath;
import deleteshorturl.apigateway.DeleteShortURL;
import deleteshorturl.dao.DeleterDao;
import deleteshorturl.services.Service;
import deleteshorturl.services.ServiceImpl;
import lombok.Getter;
import lombok.val;
import shorturls.cache.Cache;
import shorturls.config.ShortURLProperties;
import shorturls.config.ShortURLPropertiesImpl;
import shorturls.dao.Deleter;
import urlutils.idvalidator.BaseURL;
import urlutils.idvalidator.IdValidatorImpl;

/**
 * Factory class that injects the different components and finally
 * returns instances for the API Gateway 
 * DeleteShortURL and DeleteShortPath classes.
 */
public final class DeleteFactory {

	private DeleteFactory() {
		// cannot instantiate this class
	}

	/*
	 * The deleteShortURL instance to return
	 */
	@Getter(lazy = true)
	private static final DeleteShortURL deleteShortURL = initDeleteShortURL();

	/*
	 * The deleteShortPath instance to return
	 */
	@Getter(lazy = true)
	private static final DeleteShortPath deleteShortPath = initDeleteShortPath();

	/*
	 * Service layer to be injected to the API Gateway classes
	 */
	private static final  Service service = getService(new ShortURLPropertiesImpl());

	private static Service getService(ShortURLProperties props){
		val deleteFactory = props.getProperty(DELETER_FACTORY);
		val cacheEnabledStr = props.getProperty(CACHE_ENABLED);
		var deleter = new Factory<Deleter>().getInstance(deleteFactory);
		val cacheEnabled = Boolean.valueOf(cacheEnabledStr);
		if (cacheEnabled) {
			val cacheFactory = props.getProperty(CACHE_FACTORY);
			val cache = new Factory<Cache>().getInstance(cacheFactory);
			deleter = new DeleterDao(deleter, cache);
		}
		val baseURL = new BaseURL(props.getProperty(BASE_URL));
		val alphabet = props.getProperty(RANDOM_ALPHABET);
		val length = Integer.parseInt(props.getProperty(RANDOM_LENGTH));
		val idValidator = new IdValidatorImpl(baseURL, alphabet,length);
		return new ServiceImpl(deleter, idValidator);
	}
	
	private static DeleteShortURL initDeleteShortURL() {
		return new DeleteShortURL(service);
	}

	private static DeleteShortPath initDeleteShortPath() {
		return new DeleteShortPath(service);
	}

}
