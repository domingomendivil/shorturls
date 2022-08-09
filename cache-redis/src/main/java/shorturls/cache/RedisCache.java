package shorturls.cache;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

import io.lettuce.core.RedisClient;
import shorturls.model.URLItem;

public class RedisCache implements Cache{
	
	private final RedisClient redisClient;
	
	public RedisCache(RedisClient redisClient) {
		this.redisClient=redisClient;
	}

	private Optional<URLItem> parse(String result) {
		if (result==null) {
			return Optional.empty();
		}
		String[] temp = result.split(",");
		if ((temp!=null) && (temp.length==3)) {
			URLItem urlItem = new URLItem();
			try {
				urlItem.setLongURL(new URL(temp[0]));
				urlItem.setCreationDate(LocalDateTime.parse(temp[1]));
				urlItem.setExpirationHours(Long.parseLong(temp[2]));
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
		return parse(urlItem);
	}

	@Override
	public void put(String path, URLItem urlItem) {
		var connection =redisClient.connect();
		var syncCommands = connection.sync();	
		String str = format(urlItem);
		syncCommands.set(path, str);
	}

	private String format(URLItem urlItem) {
		Long expirationHours=urlItem.getExpirationHours();
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