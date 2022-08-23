package shorturls.cache.config;

import io.lettuce.core.RedisClient;
import lombok.Getter;
import shorturls.cache.RedisCache;
import shorturls.config.ShortURLProperties;
import shorturls.config.ShortURLPropertiesImpl;
/**
 * Factory for instantiating a RedisCache
 * 
 *
 */
public class RedisCacheFactory {

	private RedisCacheFactory() {
		//class cannot be instantiated
	}
	
	@Getter(lazy=true)private static final RedisCache instance = init(new ShortURLPropertiesImpl());
	
	private static RedisCache init(final ShortURLProperties props) {
		String redisURL = props.getProperty("REDIS_URL");
		return new RedisCache(RedisClient.create(redisURL));
	}
}
