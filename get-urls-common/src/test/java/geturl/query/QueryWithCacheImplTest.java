package geturl.query;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import lombok.val;
import shorturls.cache.Cache;
import shorturls.dao.Query;
import shorturls.model.URLItem;

@RunWith(MockitoJUnitRunner.class)
public class QueryWithCacheImplTest {

	@InjectMocks
	private QueryWithCacheImpl queryCache;

	@Mock
	private Query query;

	@Mock
	private Cache cache;

	@Test
	public void test1() throws MalformedURLException {
		URL url = new URL("http://www.google.com");
		URLItem urlItem = new URLItem("A", url, LocalDateTime.now(), null);
		Optional<URLItem> item = Optional.of(urlItem);
		when(cache.getById("A")).thenReturn(item);
		val res = queryCache.getById("A");
		assertEquals(item, res);
	}

	@Test
	public void test2() throws MalformedURLException {
		when(cache.getById("A")).thenReturn(Optional.empty());
		when(query.getById("A")).thenReturn(Optional.empty());
		val res = queryCache.getById("A");
		assertEquals(Optional.empty(), res);
	}

	@Test
	public void test3() throws MalformedURLException {
		URL url = new URL("http://www.google.com");
		URLItem urlItem = new URLItem("A", url, LocalDateTime.now(), null);
		Optional<URLItem> item = Optional.of(urlItem);
		when(cache.getById("A")).thenReturn(Optional.empty());
		when(query.getById("A")).thenReturn(item);
		val res = queryCache.getById("A");
		assertEquals(item, res);
	}

}
