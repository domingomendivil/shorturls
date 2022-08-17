package shorturls.cache;

import io.lettuce.core.RedisClient;
import lombok.Getter;
import shorturls.config.ShortURLProperties;

public class RedisCacheFactory {

	private RedisCacheFactory() {
		
	}
	
	@Getter(lazy=true)private static final RedisCache instance = init(new ShortURLProperties());
	
	private static RedisCache init(ShortURLProperties props) {
		String redisURL = props.getProperty("REDIS_URL");
		return new RedisCache(RedisClient.create(redisURL));
	}
}
