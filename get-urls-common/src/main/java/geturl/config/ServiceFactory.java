package geturl.config;

import static shorturls.constants.Constants.BASE_URL;
import static shorturls.constants.Constants.CACHE_ENABLED;
import static shorturls.constants.Constants.CACHE_FACTORY;
import static shorturls.constants.Constants.EVENTS_FACTORY;
import static shorturls.constants.Constants.QUERY_FACTORY;
import static shorturls.constants.Constants.RANDOM_ALPHABET;

import com.meli.events.Events;
import com.meli.factory.ConfigurationException;
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

public class ServiceFactory {

	private ServiceFactory() {
		// this class cannot be instantiated
	}
	
	
	private static final ShortURLProperties properties = new ShortURLProperties();

	@Getter(lazy = true)
	private static final Service instance = init(properties,new Factory<Query>(),new Factory<Cache>(),new Factory<Events>());

	
	static Service init(ShortURLProperties properties,Factory<Query> queryFactory,Factory<Cache> cacheFactory,Factory<Events> eventsFactory) {
		try {
			val cacheEnabled = Boolean.valueOf(properties.getProperty(CACHE_ENABLED));
			val queryFactoryStr = properties.getProperty(QUERY_FACTORY);
			var query = queryFactory.getInstance(queryFactoryStr);
			if (Boolean.TRUE.equals(cacheEnabled)) {
				val cacheFactoryStr = properties.getProperty(CACHE_FACTORY);
				val cache = cacheFactory.getInstance(cacheFactoryStr);
				query = new QueryWithCacheImpl(query, cache);
			}
			val baseURL = new BaseURL(properties.getProperty(BASE_URL));
			val eventFactoryStr = properties.getProperty(EVENTS_FACTORY);
			val events = eventsFactory.getInstance(eventFactoryStr);
			val alphabet = properties.getProperty(RANDOM_ALPHABET);
			val idValidator = new IdValidatorImpl(baseURL, alphabet);
			return new ServiceImpl(query, idValidator, events);	
		}catch (ConfigurationException e) {
			throw new shorturls.config.ConfigurationException(e);
		}


	}

}
