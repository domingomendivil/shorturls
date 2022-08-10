package redirectshorturl.apigateway;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import geturl.services.Service;
import shorturls.exceptions.InvalidArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class RedirectShortURLTest {
	

	@InjectMocks
	private RedirectShortURL redirectShortURL;
	
	@Mock
	private Service service;
	
	private HashMap<String,String> map = new HashMap<>();
	
	private APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();

	
	@Test
	public void test1() {
		input.setPathParameters(map);
		var response = redirectShortURL.handleRequest(input);
		assertEquals(Integer.valueOf(400), response.getStatusCode());
	}
	
	@Test
	public void test2() {
		input.setPathParameters(map);
		map.put("code", "");
		var response = redirectShortURL.handleRequest(input);
		assertEquals(Integer.valueOf(400), response.getStatusCode());
	}
	
	@Test
	public void test3() throws MalformedURLException, InvalidArgumentException {
		input.setPathParameters(map);
		map.put("code", "ADD");
		String responseURL ="http://www.montevideo.com.uy";
		var url = Optional.of(new URL(responseURL));
		when(service.getURL("ADD", null)).thenReturn(url);
		var response = redirectShortURL.handleRequest(input);
		assertEquals(Integer.valueOf(301), response.getStatusCode());
		assertEquals(responseURL, response.getHeaders().get("Location"));
	}
	
	@Test
	public void test4() throws MalformedURLException, InvalidArgumentException {
		input.setPathParameters(map);
		map.put("code", "ADD");
		InvalidArgumentException e = new InvalidArgumentException("Invalid URL");
		when(service.getURL("ADD", null)).thenThrow(e);
		var response = redirectShortURL.handleRequest(input);
		assertEquals(Integer.valueOf(400), response.getStatusCode());
		assertEquals("Invalid URL", response.getBody());
	}
	

	@Test
	public void test5() throws MalformedURLException, InvalidArgumentException {
		input.setPathParameters(map);
		map.put("code", "ADD");
		when(service.getURL("ADD", null)).thenReturn(Optional.empty());
		var response = redirectShortURL.handleRequest(input);
		assertEquals(Integer.valueOf(404), response.getStatusCode());
	}

	

}
