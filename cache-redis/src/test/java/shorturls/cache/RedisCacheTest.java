package shorturls.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

	@Mock
	private StatefulRedisConnection connection;

	@Mock
	private RedisCommands cmd;

	@Before
	public void set() {
		when(connection.sync()).thenReturn(cmd);
		when(client.connect()).thenReturn(connection);
	}

	@Test
	public void testGetById1() throws java.net.MalformedURLException {
		val dateStr = "2022-08-11T15:46:30";
		val url = new URL("http://www.google.com");
		val dateTime = LocalDateTime.parse(dateStr);
		val item = new URLItem("code", url, dateTime, 1L);
		val str = "http://www.google.com,2022-08-11T15:46:30,1";
		when(cmd.get("code")).thenReturn(str);
		Optional<URLItem> res = cache.getById("code");
		assertEquals(item, res.get());
	}

	@Test
	public void testGetById2() throws java.net.MalformedURLException {
		val dateStr = "2022-08-11T15:46:30";
		val url = new URL("http://www.google.com");
		val dateTime = LocalDateTime.parse(dateStr);
		val item = new URLItem("code", url, dateTime, 1L);
		when(cmd.get("code")).thenReturn(null);
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

}
