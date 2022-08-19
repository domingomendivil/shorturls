package shorturls.cache;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.SetArgs;
import lombok.val;
import shorturls.exceptions.ShortURLRuntimeException;
import shorturls.model.URLItem;

/**
 * Implementation of the shorturls.cache.Cache interface
 * for interacting with Redis Cluster
 *
 */
public class RedisCache implements Cache{
	
	private final RedisClient redisClient;
	
	public RedisCache( RedisClient redisClient) {
		this.redisClient=redisClient;
	}

	private Optional<URLItem> parse(String shortPath,String result) {
		if (result!=null) {
			String[] temp = result.split(",");
			if ((temp!=null) && (temp.length==3)) {
				try {
					val longURL = new URL(temp[0]);
					val creationDate = 	LocalDateTime.parse(temp[1]);
					val expirationHours = Long.parseLong(temp[2]);
					val urlItem= new URLItem(shortPath,longURL,creationDate,expirationHours);
					return Optional.of(urlItem);
				} catch (MalformedURLException|NumberFormatException e) {
					throw new ShortURLRuntimeException("Error getting URL from cache",e);
				}
			}
		}
		return Optional.empty();
	}
	
	@Override
	public Optional<URLItem> getById(String path) {
		val connection =redisClient.connect();
		val syncCommands = connection.sync();	
		val urlItem = syncCommands.get(path);
		return parse(path,urlItem);
	}

	@Override
	public void put(String path, URLItem urlItem) {
		val connection =redisClient.connect();
		val asyncCmds = connection.async();	
		val str = format(urlItem);
		val expirationTime=urlItem.getExpirationTime();
		if (expirationTime==null){
			RedisFuture<String> success=asyncCmds.set(path, str);
		}else{
			SetArgs setArgs= SetArgs.Builder.exAt(expirationTime);
			RedisFuture<String> success=asyncCmds.set(path, str, setArgs);
		}	
		
	}

	private String format(URLItem urlItem) {
		var expirationHours=urlItem.getExpirationTime();
		if (expirationHours==null){
			expirationHours=0L;
		}
		return String.format("%s,%s,%s",urlItem.getLongURL(),urlItem.getCreationDate().toString(),expirationHours);
	}

	@Override
	public boolean delete(String path) {
		val connection =redisClient.connect();
		val syncCommands = connection.sync();
		return syncCommands.del(path) > 0;
	}

}
