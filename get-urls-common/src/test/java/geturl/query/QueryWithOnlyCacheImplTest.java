package geturl.query;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;

import shorturls.cache.Cache;
import shorturls.model.URLItem;


@RunWith(MockitoJUnitRunner.class)
public class QueryWithOnlyCacheImplTest {
	
	@InjectMocks
	private QueryWithOnlyCacheImpl query;

	@Mock
	private Cache cache;
	
	
	@Test
	public void test1() {
		when(cache.getById("code")).thenReturn(Optional.empty());
		Optional<URLItem> item = query.getById("code");
		assertEquals(Optional.empty(),item);
	}
	
	@Test
	public void test2() throws MalformedURLException {
		URL url = new URL("http://www.google.com");
		URLItem anItem = new URLItem("code",url,LocalDateTime.now(),231L);
		when(cache.getById("code")).thenReturn(Optional.of(anItem));
		Optional<URLItem> item = query.getById("code");
		assertEquals(Optional.of(anItem),item);
	}

}
