package createshorturl.services;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import createshorturl.events.Events;
import createshorturl.generator.IDGenerator;
import shorturls.exceptions.InvalidArgumentException;
import urlutils.idvalidator.BaseURL;

@RunWith(MockitoJUnitRunner.class)
public class ServiceImplTest {
	

	
	@Mock
	private Events events;

	@Mock
	private IDGenerator generator;
	
	@Mock
	private BaseURL baseURL;
	
	
	@InjectMocks
	private ServiceImpl svc;

	@Test
	public void testCreateShortURL1() throws MalformedURLException, InvalidArgumentException {
		when(generator.generateUniqueID()).thenReturn("DFJKSX");
		when(baseURL.toURL()).thenReturn("http://localhost:8000/");
		URL url = svc.createShortURL(new URL("http://www.montevideo.com.uy"));
		assertEquals(new URL("http://localhost:8000/DFJKSX"), url);
	}

	@ParameterizedTest
	@ValueSource(strings= {"file:///sdcard","ftp://site.edu.uy"})
	public void testCreateShortURL2(String arg) throws MalformedURLException, InvalidArgumentException {
		assertThrows(InvalidArgumentException.class, () -> svc.createShortURL(new URL(arg)));
	}

	@ParameterizedTest
	@ValueSource(longs= {0L,1L})
	public void testCreateShortURL3(long arg) throws MalformedURLException, InvalidArgumentException {
		assertThrows(InvalidArgumentException.class, () ->svc.createShortURL(new URL("http://www.montevideo.com.uy"), arg));
	}


	@ParameterizedTest
	@ValueSource(strings= {"http://www.montevideo.com.uy","https://www.montevideo.com.uy"})
	public void testCreateShortURL6(String arg) throws MalformedURLException, InvalidArgumentException {
		when(generator.generateUniqueID()).thenReturn("DFJKSX");
		when(baseURL.toURL()).thenReturn("http://localhost:8000/");
		var url = svc.createShortURL(new URL(arg), 10000L);
		assertEquals(new URL("http://localhost:8000/DFJKSX"),url);
	}
	
	
	@Test
	public void testCreateShortURL9() throws MalformedURLException, InvalidArgumentException {
		when(generator.generateUniqueID()).thenReturn("DFJKSX");
		when(baseURL.toURL()).thenReturn("http://localhost:8000/");
		var url = svc.createShortURL(new URL("https://www.montevideo.com.uy"), 99999999999L);
		assertEquals(new URL("http://localhost:8000/DFJKSX"),url);
	}

}
