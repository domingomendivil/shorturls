package shorturls.cache;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

import io.lettuce.core.RedisClient;
import io.lettuce.core.SetArgs;
import lombok.val;
import shorturls.model.URLItem;

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
		var connection =redisClient.connect();
		var syncCommands = connection.sync();	
		String urlItem = syncCommands.get(path);
		System.out.println("item obtained "+urlItem);
		return parse(path,urlItem);
	}

	@Override
	public void put(String path, URLItem urlItem) {
		var connection =redisClient.connect();
		var syncCommands = connection.sync();	
		String str = format(urlItem);
		System.out.println("redis put "+str);
		Long expirationTime=urlItem.getExpirationTime();
		if (expirationTime==null){
			syncCommands.set(path, str);
		}else{
			SetArgs setArgs= SetArgs.Builder.exAt(expirationTime);
			syncCommands.set(path, str, setArgs);
		}	
		
	}

	private String format(URLItem urlItem) {
		Long expirationHours=urlItem.getExpirationTime();
		if (expirationHours==null){
			expirationHours=0L;
		}
		return String.format("%s,%s,%s",urlItem.getLongURL(),urlItem.getCreationDate().toString(),expirationHours);
	}

	@Override
	public boolean delete(String path) {
		var connection =redisClient.connect();
		var syncCommands = connection.sync();
		return syncCommands.del(path) > 0;
	}

}
