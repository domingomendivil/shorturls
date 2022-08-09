package createshorturl.config;

import static java.lang.System.getenv;
import static shorturls.constants.Constants.BASE_URL;
import static shorturls.constants.Constants.CACHE_ENABLED;
import static shorturls.constants.Constants.CACHE_FACTORY;
import static shorturls.constants.Constants.WRITER_FACTORY;

import com.meli.factory.Factory;

import createshorturl.apigateway.CreateShortURL;
import createshorturl.apigateway.CreateShortURLHours;
import createshorturl.events.EventsImplDAO;
import createshorturl.events.WriterWithCache;
import createshorturl.generator.IDGenerator;
import createshorturl.services.BaseURL;
import createshorturl.services.Service;
import createshorturl.services.ServiceImpl;
import lombok.Getter;
import lombok.val;
import shorturls.cache.Cache;
import shorturls.dao.Writer;

public class CreateFactory {

	private CreateFactory() {
		// this class cannot be instantiated
	}

	@Getter(lazy = true)
	private static final CreateShortURL createShortURL = new CreateShortURL(getService());

	@Getter(lazy = true)
	private static final CreateShortURLHours createShortURLHours = new CreateShortURLHours(getService());

	@Getter(lazy = true)
	private static final Service service = initService();

	private static Service initService() {
		val cacheEnabled = Boolean.valueOf(getenv(CACHE_ENABLED));
		val baseURL = new BaseURL(getenv(BASE_URL));
		var writerFactory = getenv(WRITER_FACTORY);
		var writer = new Factory<Writer>().getInstance(writerFactory);
		if (Boolean.TRUE.equals(cacheEnabled)) {
			val cacheFactory = getenv(CACHE_FACTORY);
			val cache = new Factory<Cache>().getInstance(cacheFactory);
			writer = new WriterWithCache(writer, cache);
		}
		val events = new EventsImplDAO(writer);
		val idGeneratorFactory = getenv("ID_GENERATOR_FACTORY");
		val idGenerator = new Factory<IDGenerator>().getInstance(idGeneratorFactory);
		return new ServiceImpl(events, idGenerator, baseURL);
	}

}
