package shorturls.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.val;
import shorturls.exceptions.ShortURLRuntimeException;
import shorturls.model.URLItem;

@RunWith(MockitoJUnitRunner.class)
public class RedisCacheTest {

	@InjectMocks
	private RedisCache cache;

	@Mock
	private RedisClient client;

	@Mock
	private StatefulRedisConnection connection;

	@Mock
	private RedisCommands cmd;
	
	@Mock
	private RedisAsyncCommands asyncCmd;


	@Before
	public void set() {
		when(connection.sync()).thenReturn(cmd);
		when(connection.async()).thenReturn(asyncCmd);
		when(client.connect()).thenReturn(connection);
	}
	
	
	private URLItem getURLItem(String code,String urlStr,String dateStr,Long seconds) throws java.net.MalformedURLException {
		val url = new URL(urlStr);
		val dateTime = LocalDateTime.parse(dateStr);
		val item = new URLItem(code, url, dateTime, seconds);
		return item;
	}

	@Test
	public void testGetById1() throws java.net.MalformedURLException {
		val item = getURLItem("code","http://www.google.com", "2022-08-11T15:46:30", 1L);
		val str = "http://www.google.com,2022-08-11T15:46:30,1";
		when(cmd.get("code")).thenReturn(str);
		Optional<URLItem> res = cache.getById("code");
		assertEquals(item, res.get());
	}

	@Test
	public void testGetById2() throws java.net.MalformedURLException {
		val item = getURLItem("code","http://www.google.com", "2022-08-11T15:46:30", 1L);
		when(cmd.get("code")).thenReturn(null);
		Optional<URLItem> res = cache.getById("code");
		assertEquals(Optional.empty(), res);
	}
	
	@Test(expected = ShortURLRuntimeException.class)
	public void testGetById3() throws java.net.MalformedURLException {
		val item = getURLItem("code","http://www.google.com", "2022-08-11T15:46:30", 1L);
		when(cmd.get("code")).thenReturn("urlerro,2022-08-11T15:46:30,1");
		Optional<URLItem> res = cache.getById("code");
	}
	
	@Test
	public void testGetById4() throws java.net.MalformedURLException {
		val item = getURLItem("code","http://www.google.com", "2022-08-11T15:46:30", 1L);
		when(cmd.get("code")).thenReturn(null);
		Optional<URLItem> res = cache.getById("code");
		assertEquals(Optional.empty(), res);
	}
	
	@Test
	public void testGetById5() throws java.net.MalformedURLException {
		val item = getURLItem("code","http://www.google.com", "2022-08-11T15:46:30", 1L);
		when(cmd.get("code")).thenReturn("code,code");
		Optional<URLItem> res = cache.getById("code");
		assertEquals(Optional.empty(), res);
	}

	@Test
	public void testDeleteById1() throws java.net.MalformedURLException {
		when(cmd.del("code")).thenReturn(1L);
		boolean res = cache.delete("code");
		assertTrue(res);
	}

	@Test
	public void testDeleteById2() throws java.net.MalformedURLException {
		when(cmd.del("code")).thenReturn(0L);
		boolean res = cache.delete("code");
		assertFalse(res);
	}
	
	@Test
	public void testPut1() throws java.net.MalformedURLException{
		val item = getURLItem("code","http://www.google.com", "2022-08-11T15:46:30", 1L);
		cache.put("code",item);
	}
	
	@Test
	public void testPut2() throws java.net.MalformedURLException{
		val item = getURLItem("code","http://www.google.com", "2022-08-11T15:46:30",null);
		cache.put("code",item);
	}

}
