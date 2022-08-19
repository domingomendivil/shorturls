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
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import geturl.services.Service;
import shorturls.config.ShortURLProperties;
import shorturls.exceptions.InvalidArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class RedirectShortURLTest {
	

	@InjectMocks
	private RedirectShortURL redirectShortURL;

	@Mock
	private ShortURLProperties props;
	
	@Mock
	private Service service;
	
	private HashMap<String,String> map = new HashMap<>();
	
	private APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();

	private void assertResponse(int statusCode,APIGatewayProxyResponseEvent response) {
		assertEquals(Integer.valueOf(statusCode), response.getStatusCode());
	}
	
	private void whenInvokeServiceReturnURL(String code,String responseUrl) throws InvalidArgumentException, MalformedURLException {
		input.setPathParameters(map);
		map.put("code", code);
		var url = Optional.of(new URL(responseUrl));
		when(service.getURL("ADD", null)).thenReturn(url);
	}
	
	@Test
	public void test1() {
		input.setPathParameters(map);
		var response = redirectShortURL.handleRequest(input);
		assertResponse(400,response);
	}
	
	@Test
	public void test2() {
		input.setPathParameters(map);
		map.put("code", "");
		var response = redirectShortURL.handleRequest(input);
		assertResponse(400,response);
	}
	
	@Test
	public void test3() throws MalformedURLException, InvalidArgumentException {
		whenInvokeServiceReturnURL("ADD", "http://www.montevideo.com.uy");
		var response = redirectShortURL.handleRequest(input);
		assertResponse(301,response);
		assertEquals("http://www.montevideo.com.uy", response.getHeaders().get("Location"));
	}
	
	@Test
	public void test4() throws MalformedURLException, InvalidArgumentException {
		input.setPathParameters(map);
		map.put("code", "ADD");
		InvalidArgumentException e = new InvalidArgumentException("Invalid URL");
		when(service.getURL("ADD", null)).thenThrow(e);
		var response = redirectShortURL.handleRequest(input);
		assertResponse(400,response);
		assertEquals("Invalid URL", response.getBody());
	}
	

	@Test
	public void test5() throws MalformedURLException, InvalidArgumentException {
		input.setPathParameters(map);
		map.put("code", "ADD");
		when(service.getURL("ADD", null)).thenReturn(Optional.empty());
		var response = redirectShortURL.handleRequest(input);
		assertResponse(404,response);
	}

	

}
