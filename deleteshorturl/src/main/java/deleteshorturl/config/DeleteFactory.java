package deleteshorturl.config;

import static java.lang.System.getenv;
import static shorturls.constants.Constants.BASE_URL;
import static shorturls.constants.Constants.CACHE_ENABLED;
import static shorturls.constants.Constants.CACHE_FACTORY;
import static shorturls.constants.Constants.DELETER_FACTORY;
import static shorturls.constants.Constants.RANDOM_ALPHABET;

import com.meli.factory.Factory;

import deleteshorturl.apigateway.DeleteShortPath;
import deleteshorturl.apigateway.DeleteShortURL;
import deleteshorturl.dao.DeleterDao;
import deleteshorturl.services.Service;
import deleteshorturl.services.ServiceImpl;
import lombok.Getter;
import lombok.val;
import shorturls.cache.Cache;
import shorturls.dao.Deleter;
import urlutils.idvalidator.BaseURL;
import urlutils.idvalidator.IdValidatorImpl;

public final class DeleteFactory {

	private DeleteFactory() {
		// cannot instantiate this class
	}

	@Getter(lazy = true)
	private static final DeleteShortURL deleteShortURL = initDeleteShortURL();

	@Getter(lazy = true)
	private static final DeleteShortPath deleteShortPath = initDeleteShortPath();

	private final static Service service = getService();

	private static Service getService(){
		val deleteFactory = getenv(DELETER_FACTORY);
		val cacheEnabledStr = getenv(CACHE_ENABLED);
		var deleter = new Factory<Deleter>().getInstance(deleteFactory);
		val cacheEnabled = Boolean.valueOf(cacheEnabledStr);
		if (cacheEnabled) {
			val cacheFactory = getenv(CACHE_FACTORY);
			val cache = new Factory<Cache>().getInstance(cacheFactory);
			deleter = new DeleterDao(deleter, cache);
		}
		val baseURL = new BaseURL(getenv(BASE_URL));
		val alphabet = getenv(RANDOM_ALPHABET);
		val idValidator = new IdValidatorImpl(baseURL, alphabet);
		return new ServiceImpl(deleter, idValidator);
	}
	
	private static DeleteShortURL initDeleteShortURL() {
		return new DeleteShortURL(service);
	}

	private static DeleteShortPath initDeleteShortPath() {
		return new DeleteShortPath(service);
	}

}
