package geturl.config;

import static shorturls.constants.Constants.BASE_URL;
import static shorturls.constants.Constants.CACHE_ENABLED;
import static shorturls.constants.Constants.CACHE_FACTORY;
import static shorturls.constants.Constants.EVENTS_FACTORY;
import static shorturls.constants.Constants.QUERY_FACTORY;
import static shorturls.constants.Constants.RANDOM_ALPHABET;
import static shorturls.constants.Constants.CACHE_ONLY;
import com.meli.events.Events;
import com.meli.factory.Factory;

import geturl.query.QueryWithCacheImpl;
import geturl.query.QueryWithOnlyCacheImpl;
import geturl.services.Service;
import geturl.services.ServiceImpl;
import lombok.Getter;
import lombok.val;
import shorturls.cache.Cache;
import shorturls.config.ShortURLProperties;
import shorturls.dao.Query;
import urlutils.idvalidator.BaseURL;
import urlutils.idvalidator.IdValidatorImpl;

/**
 * Factory class for instantiating the Service Layer.
 * 
 *
 */
public class ServiceFactory {

	private ServiceFactory() {
		// this class cannot be instantiated
	}
	
	
	/*
	 * Lombok Getter is used for lazily instantiating the Service Layer
	 */
	@Getter(lazy = true)
	private static final Service instance = init(new ShortURLProperties(),new Factory<Query>(),new Factory<Cache>(),new Factory<Events>());

	/*
	 * Method for creating the Service layer
	 */
	static Service init(ShortURLProperties properties,Factory<Query> queryFactory,Factory<Cache> cacheFactory,Factory<Events> eventsFactory) {
		try {
			val query = getQuery(properties, queryFactory, cacheFactory);
			val baseURL = new BaseURL(properties.getProperty(BASE_URL));
			val eventFactoryStr = properties.getProperty(EVENTS_FACTORY);
			val events = eventsFactory.getInstance(eventFactoryStr);
			val alphabet = properties.getProperty(RANDOM_ALPHABET);
			val idValidator = new IdValidatorImpl(baseURL, alphabet);
			return new ServiceImpl(query, idValidator, events);	
		}catch (com.meli.factory.ConfigurationException e) {
			e.printStackTrace();
			throw new shorturls.config.ConfigurationException(e);
		}
	}

	/* 
	 * Gets the Query interface to be injected in the Service Layer
	 */
	private static Query getQuery(ShortURLProperties properties,Factory<Query> queryFactory,Factory<Cache> cacheFactory){
		val cacheEnabled = Boolean.valueOf(properties.getProperty(CACHE_ENABLED));
		val cacheOnly = Boolean.valueOf(properties.getProperty(CACHE_ONLY));
		val queryFactoryStr = properties.getProperty(QUERY_FACTORY);
		val query = queryFactory.getInstance(queryFactoryStr);
		if (Boolean.TRUE.equals(cacheEnabled)) {
			val cacheFactoryStr = properties.getProperty(CACHE_FACTORY);
			val cache = cacheFactory.getInstance(cacheFactoryStr);
			if (Boolean.TRUE.equals(cacheOnly)){
				return new QueryWithOnlyCacheImpl(cache);
			}else
				return new QueryWithCacheImpl(query, cache);
		}
		return query;
	}

}
