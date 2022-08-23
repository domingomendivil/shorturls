package shorturls.cache;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

import io.lettuce.core.RedisClient;
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


	/**
	 * Parses a string (gotten from the redis cache) in the format 
	 * where "shortPath,longURL,creationDate,epchSeconds".
	 * E.g. "klSEv7,http://www.google.com,2012-03-02 09:23:00:01,161232"
	 * and gets an URLItem from it.
	 * In case that that the string is invalid it throws a ShortURLRuntimeException
	 * @param shortPath
	 * @param result
	 * @return
	 */
	private Optional<URLItem> parse(final String shortPath,final String result) {
		if (result!=null) {
			String[] temp = result.split(",");
			if ((temp!=null) && (temp.length==3)) {
				try {
					val longURL = new URL(temp[0]);
					val creationDate = 	LocalDateTime.parse(temp[1]);
					Long seconds = Long.parseLong(temp[2]);
					if (seconds == 0L){
						seconds=null;
					}
					val urlItem= new URLItem(shortPath,longURL,creationDate,seconds);
					return Optional.of(urlItem);
				} catch (MalformedURLException|NumberFormatException e) {
					throw new ShortURLRuntimeException("Error getting URL from cache",e);
				}
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Gets an Optional URLItem given its path (code of the short URL) 
	 */
	@Override
	public Optional<URLItem> getById(final String path) {
		val connection =redisClient.connect();
		val syncCommands = connection.sync();	
		val urlItem = syncCommands.get(path);
		return parse(path,urlItem);
	
	}

	@Override
	
	public void put(final String path, final URLItem urlItem) {
		val connection =redisClient.connect();
		val cmds = connection.sync();	
		val str = format(urlItem);
		val expirationTime=urlItem.getExpirationTime();
		if (expirationTime==null){
			cmds.set(path, str);
		}else{
			SetArgs setArgs= SetArgs.Builder.exAt(expirationTime);
			cmds.set(path, str, setArgs);
		}	
	}

	private String format(final URLItem urlItem) {
		var expirationHours=urlItem.getExpirationTime();
		if (expirationHours==null){
			expirationHours=0L;
		}
		return String.format("%s,%s,%s",urlItem.getLongURL(),urlItem.getCreationDate().toString(),expirationHours);
	}

	@Override
	public boolean delete(final String path) {
		val connection =redisClient.connect();
		val syncCommands = connection.sync();
		return syncCommands.del(path) > 0;
	}

}
