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
		if (result==null) {
			return Optional.empty();
		}
		String[] temp = result.split(",");
		if ((temp!=null) && (temp.length==3)) {
			try {
				val longURL = new URL(temp[0]);
				val creationDate = 	LocalDateTime.parse(temp[1]);
				val expirationHours = Long.parseLong(temp[2]);
				val urlItem= new URLItem(shortPath,longURL,creationDate,expirationHours);
				return Optional.of(urlItem);
			} catch (MalformedURLException|NumberFormatException e) {
				//error reading from Redis Server. Should never happen
				e.printStackTrace();
			}
		}
		return Optional.empty();
	}
	
	@Override
	public Optional<URLItem> getById(String path) {
		System.out.println("redis cache getById "+path);
		val connection =redisClient.connect();
		val syncCommands = connection.sync();	
		val urlItem = syncCommands.get(path);
		System.out.println("item obtenido "+urlItem);
		return parse(path,urlItem);
	}

	@Override
	public void put(String path, URLItem urlItem) {
		System.out.println("put short path "+path);
		val connection =redisClient.connect();
		val asyncCmds = connection.async();	
		val str = format(urlItem);
		val expirationTime=urlItem.getExpirationTime();
		System.out.println("expiration time "+expirationTime);
		if (expirationTime==null){
			System.out.println("set sin expirationTime");
			RedisFuture<String> success=asyncCmds.set(path, str);
			try {
				System.out.println("put con time "+success.get());
			} catch (InterruptedException|ExecutionException e) {
				throw new ShortURLRuntimeException("Error inserting in redis cache",e);
			} 
		}else{
			System.out.println("set con expiration time ");
			SetArgs setArgs= SetArgs.Builder.exAt(expirationTime);
			RedisFuture<String> success=asyncCmds.set(path, str, setArgs);
			try {
				System.out.println("put con time "+success.get());
			} catch (InterruptedException|ExecutionException e) {
				throw new ShortURLRuntimeException("Error inserting in redis cache",e);
			} 
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
