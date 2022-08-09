package geturl.config;

import static java.lang.System.getenv;
import static shorturls.constants.Constants.BASE_URL;
import static shorturls.constants.Constants.CACHE_ENABLED;
import static shorturls.constants.Constants.CACHE_FACTORY;
import static shorturls.constants.Constants.EVENTS_FACTORY;
import static shorturls.constants.Constants.QUERY_FACTORY;
import static shorturls.constants.Constants.RANDOM_ALPHABET;

import com.meli.events.Events;
import com.meli.factory.Factory;

import geturl.query.QueryWithCacheImpl;
import geturl.services.Service;
import geturl.services.ServiceImpl;
import lombok.Getter;
import lombok.val;
import shorturls.cache.Cache;
import shorturls.dao.Query;
import urlutils.idvalidator.BaseURL;
import urlutils.idvalidator.IdValidatorImpl;

public class ServiceFactory {

	private ServiceFactory() {
		// this class cannot be instantiated
	}

	@Getter(lazy = true)
	private static final Service instance = init();

	private static Service init() {
		val cacheEnabled = Boolean.valueOf(getenv(CACHE_ENABLED));
		val queryFactory = getenv(QUERY_FACTORY);
		var query = new Factory<Query>().getInstance(queryFactory);
		if (Boolean.TRUE.equals(cacheEnabled)) {
			val cacheFactory = getenv(CACHE_FACTORY);
			val cache = new Factory<Cache>().getInstance(cacheFactory);
			query = new QueryWithCacheImpl(query, cache);
		}
		val baseURL = new BaseURL(getenv(BASE_URL));
		val eventFactory = getenv(EVENTS_FACTORY);
		val events = new Factory<Events>().getInstance(eventFactory);
		val alphabet = getenv(RANDOM_ALPHABET);
		val idValidator = new IdValidatorImpl(baseURL, alphabet);
		return new ServiceImpl(query, idValidator, events);

	}

}
