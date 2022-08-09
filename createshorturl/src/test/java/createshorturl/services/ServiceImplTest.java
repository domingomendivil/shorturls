package createshorturl.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import createshorturl.events.Events;
import createshorturl.generator.IDGenerator;

@RunWith(MockitoJUnitRunner.class)
public class ServiceImplTest {
	
	@InjectMocks
	private ServiceImpl svc;
	
	@Mock
	private Events events;

	@Mock
	private IDGenerator generator;
	
	@Mock
	private BaseURL baseURL;
	
	
	@Test
	public void testCreateShortURL1() throws MalformedURLException, InvalidArgumentsException {
		when(generator.generateUniqueID()).thenReturn("DFJKSX");
		when(baseURL.toURL()).thenReturn("http://localhost:8000/");
		URL url = svc.createShortURL(new URL("http://www.montevideo.com.uy"));
		assertEquals(new URL("http://localhost:8000/DFJKSX"), url);
	}

	@Test(expected = InvalidArgumentsException.class)
	public void testCreateShortURL2() throws MalformedURLException, InvalidArgumentsException {
		svc.createShortURL(new URL("file:///sdcard"));
	}

	@Test(expected = InvalidArgumentsException.class)
	public void testCreateShortURL3() throws MalformedURLException, InvalidArgumentsException {
		svc.createShortURL(new URL("http://www.montevideo.com.uy"), -1L);
	}

	@Test(expected = InvalidArgumentsException.class)
	public void testCreateShortURL4() throws MalformedURLException, InvalidArgumentsException {
		svc.createShortURL(new URL("http://www.montevideo.com.uy"), 0L);
	}

	@Test(expected = InvalidArgumentsException.class)
	public void testCreateShortURL5() throws MalformedURLException, InvalidArgumentsException {
		svc.createShortURL(new URL("http://www.montevideo.com.uy"), 10001L);
	}

	@Test
	public void testCreateShortURL6() throws MalformedURLException, InvalidArgumentsException {
		when(generator.generateUniqueID()).thenReturn("DFJKSX");
		when(baseURL.toURL()).thenReturn("http://localhost:8000/");
		var url = svc.createShortURL(new URL("http://www.montevideo.com.uy"), 10000L);
		assertEquals(new URL("http://localhost:8000/DFJKSX"),url);
	}

}
