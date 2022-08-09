package shorturls.cache;

import io.lettuce.core.RedisClient;

public class RedisCacheFactory {

	private RedisCacheFactory() {
		
	}
	
	private static RedisCache redisCache;
	
	public static synchronized RedisCache getInstance() {
		if (redisCache==null) {
			String redisURL = System.getenv("REDIS_URL");
			redisCache =new RedisCache(RedisClient.create(redisURL));
		}
		return redisCache;
	}
}
