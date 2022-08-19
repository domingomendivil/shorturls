package geturl.config;

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
			val cacheEnabled = Boolean.valueOf(properties.getProperty(CACHE_ENABLED));
			val queryFactoryStr = properties.getProperty(QUERY_FACTORY);
			var query = queryFactory.getInstance(queryFactoryStr);
			if (Boolean.TRUE.equals(cacheEnabled)) {
				System.out.println("cache enabled");
				val cacheFactoryStr = properties.getProperty(CACHE_FACTORY);
				val cache = cacheFactory.getInstance(cacheFactoryStr);
				query = new QueryWithCacheImpl(query, cache);
			}else{
				System.out.println("cache not enabled");
			}
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

}
