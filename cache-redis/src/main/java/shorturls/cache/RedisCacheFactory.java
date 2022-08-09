package shorturls.cache;

import io.lettuce.core.RedisClient;
import lombok.Getter;

public class RedisCacheFactory {

	private RedisCacheFactory() {
		
	}
	
	@Getter(lazy=true)private static final RedisCache instance = init();
	
	private static RedisCache init() {
		String redisURL = System.getenv("REDIS_URL");
		return new RedisCache(RedisClient.create(redisURL));
	}
}
