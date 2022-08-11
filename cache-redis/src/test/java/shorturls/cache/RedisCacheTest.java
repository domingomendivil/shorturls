package shorturls.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.val;
import shorturls.model.URLItem;

@RunWith(MockitoJUnitRunner.class)
public class RedisCacheTest {
	
	@InjectMocks
	private RedisCache cache;
	
	@Mock
	private RedisClient client;
	
	@Test 
	public void testGetById1() throws java.net.MalformedURLException
	{
			val dateStr = "2022-08-11T15:46:30";
			val url = new URL("http://www.google.com");
			val dateTime= LocalDateTime.parse(dateStr);
			val  item = new URLItem("code",url,dateTime,1L);
			val connection  =Mockito.mock(StatefulRedisConnection.class);
			val cmd =mock(RedisCommands.class);
			val str= "http://www.google.com,2022-08-11T15:46:30,1";
			when(cmd.get("code")).thenReturn(str);
			when(connection.sync()).thenReturn(cmd);
			when(client.connect()).thenReturn(connection);
			Optional<URLItem> res = cache.getById("code");
			assertEquals(item, res.get());
	}
	
	@Test 
	public void testGetById2() throws java.net.MalformedURLException
	{
			val dateStr = "2022-08-11T15:46:30";
			val url = new URL("http://www.google.com");
			val dateTime= LocalDateTime.parse(dateStr);
			val  item = new URLItem("code",url,dateTime,1L);
			val connection  =Mockito.mock(StatefulRedisConnection.class);
			val cmd =mock(RedisCommands.class);
			when(cmd.get("code")).thenReturn(null);
			when(connection.sync()).thenReturn(cmd);
			when(client.connect()).thenReturn(connection);
			Optional<URLItem> res = cache.getById("code");
			assertEquals(Optional.empty(), res);
	}
	

}
