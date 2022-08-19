package createshorturl.config;

import static shorturls.constants.Constants.BASE_URL;
import static shorturls.constants.Constants.CACHE_ENABLED;
import static shorturls.constants.Constants.CACHE_FACTORY;
import static shorturls.constants.Constants.WRITER_FACTORY;

import com.meli.factory.Factory;

import createshorturl.apigateway.CreateShortURL;
import createshorturl.apigateway.CreateShortURLSeconds;
import createshorturl.events.EventsImplDAO;
import createshorturl.events.WriterWithCache;
import createshorturl.generator.IDGenerator;
import createshorturl.services.Service;
import createshorturl.services.ServiceImpl;
import lombok.Getter;
import lombok.val;
import shorturls.cache.Cache;
import shorturls.config.ShortURLProperties;
import shorturls.config.ShortURLPropertiesImpl;
import shorturls.dao.Writer;
import urlutils.idvalidator.BaseURL;

public class CreateFactory {

	private CreateFactory() {
		// this class cannot be instantiated
	}

	@Getter(lazy = true)
	private static final CreateShortURL createShortURL = new CreateShortURL(getService(),new ShortURLPropertiesImpl());

	@Getter(lazy = true)
	private static final CreateShortURLSeconds createShortURLSeconds = new CreateShortURLSeconds(getService());

	@Getter(lazy = true)
	private static final Service service = initService(new ShortURLPropertiesImpl());

	/*
	 * Creates the Service layer
	 */
	private static Service initService(ShortURLProperties props) {
		val baseURL = new BaseURL(props.getProperty(BASE_URL));
		val writer = getWriter(props);
		val events = new EventsImplDAO(writer);
		val idGeneratorFactory = props.getProperty("ID_GENERATOR_FACTORY");
		val idGenerator = new Factory<IDGenerator>().getInstance(idGeneratorFactory);
		return new ServiceImpl(events, idGenerator, baseURL);
	}

	/*
	 * Gets the Writer interface to inject to the service layer
	 */
	private static Writer getWriter(ShortURLProperties props){
		val cacheEnabled = Boolean.valueOf(props.getProperty(CACHE_ENABLED));
		val writerFactory = props.getProperty(WRITER_FACTORY);
		val writer = new Factory<Writer>().getInstance(writerFactory);
		if (Boolean.TRUE.equals(cacheEnabled)) {
			val cacheFactory = props.getProperty(CACHE_FACTORY);
			val cache = new Factory<Cache>().getInstance(cacheFactory);
			return  new WriterWithCache(writer, cache);
		}
		return writer;
	}

}
