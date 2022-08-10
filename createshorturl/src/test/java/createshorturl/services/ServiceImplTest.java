package createshorturl.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import createshorturl.events.Events;
import createshorturl.generator.IDGenerator;
import shorturls.exceptions.InvalidArgumentException;

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
	public void testCreateShortURL1() throws MalformedURLException, InvalidArgumentException {
		when(generator.generateUniqueID()).thenReturn("DFJKSX");
		when(baseURL.toURL()).thenReturn("http://localhost:8000/");
		URL url = svc.createShortURL(new URL("http://www.montevideo.com.uy"));
		assertEquals(new URL("http://localhost:8000/DFJKSX"), url);
	}

	@Test(expected = InvalidArgumentException.class)
	public void testCreateShortURL2() throws MalformedURLException, InvalidArgumentException {
		svc.createShortURL(new URL("file:///sdcard"));
	}

	@Test(expected = InvalidArgumentException.class)
	public void testCreateShortURL3() throws MalformedURLException, InvalidArgumentException {
		svc.createShortURL(new URL("http://www.montevideo.com.uy"), -1L);
	}

	@Test(expected = InvalidArgumentException.class)
	public void testCreateShortURL4() throws MalformedURLException, InvalidArgumentException {
		svc.createShortURL(new URL("http://www.montevideo.com.uy"), 0L);
	}

	@Test(expected = InvalidArgumentException.class)
	public void testCreateShortURL5() throws MalformedURLException, InvalidArgumentException {
		svc.createShortURL(new URL("http://www.montevideo.com.uy"), 10001L);
	}

	@Test
	public void testCreateShortURL6() throws MalformedURLException, InvalidArgumentException {
		when(generator.generateUniqueID()).thenReturn("DFJKSX");
		when(baseURL.toURL()).thenReturn("http://localhost:8000/");
		var url = svc.createShortURL(new URL("http://www.montevideo.com.uy"), 10000L);
		assertEquals(new URL("http://localhost:8000/DFJKSX"),url);
	}
	
	@Test
	public void testCreateShortURL7() throws MalformedURLException, InvalidArgumentException {
		when(generator.generateUniqueID()).thenReturn("DFJKSX");
		when(baseURL.toURL()).thenReturn("http://localhost:8000/");
		var url = svc.createShortURL(new URL("https://www.montevideo.com.uy"), 10000L);
		assertEquals(new URL("http://localhost:8000/DFJKSX"),url);
	}
	
	@Test(expected=InvalidArgumentException.class)
	public void testCreateShortURL8() throws MalformedURLException, InvalidArgumentException {
		var url = svc.createShortURL(new URL("ftp://site.edu.uy"), 10000L);
		assertEquals(new URL("http://localhost:8000/DFJKSX"),url);
	}

}
