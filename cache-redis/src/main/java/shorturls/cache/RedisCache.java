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

	private Optional<URLItem> parse(String shortPath,String result) {
		System.out.println("parsing shortpath "+shortPath +"result "+result);
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
					System.out.println("url iten obtenido");
					return Optional.of(urlItem);
				} catch (MalformedURLException|NumberFormatException e) {
					System.out.println("error numberformtat");
					e.printStackTrace();
					throw new ShortURLRuntimeException("Error getting URL from cache",e);
				}
			}
		}
		System.out.println("optional.empty()");
		return Optional.empty();
	}
	
	@Override
	public Optional<URLItem> getById(String path) {
		System.out.println("redis getById");
		val connection =redisClient.connect();
		System.out.println("luego de redisClient.connect()");
		val syncCommands = connection.sync();	
		System.out.println("luego de connection.sync()");
		val urlItem = syncCommands.get(path);
		System.out.println("luego de syncCommands.get(path)");
		return parse(path,urlItem);
	}

	@Override
	public void put(String path, URLItem urlItem) {
		System.out.println("redis put");
		val connection =redisClient.connect();
		val cmds = connection.sync();	
		val str = format(urlItem);
		val expirationTime=urlItem.getExpirationTime();
		if (expirationTime==null){
			System.out.println("expirtion time nulls");
			System.out.println();
			cmds.set(path, str);
			System.out.println("luego de put en cache sin expiration");
		}else{
			System.out.println("antes de put en cache con expiration");
			SetArgs setArgs= SetArgs.Builder.exAt(expirationTime);
			cmds.set(path, str, setArgs);
			System.out.println("luego de put en cache con expiration");
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
