package geturl.apigateway;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import geturl.services.Service;
import lombok.val;
import shorturls.exceptions.InvalidArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class GetURLTest {

	@InjectMocks
	private GetURL getURL;

	@Mock
	private Service svc;

	@Test
	public void test1() throws MalformedURLException, InvalidArgumentException {
		val shortPath="HSFSF";
		val e = new InvalidArgumentException("Invalid URL");
		when(svc.getURL(shortPath, null)).thenThrow(e);
		val input = new APIGatewayProxyRequestEvent();
		Map<String, String> map = new HashMap<>();
		map.put("code", shortPath);
		input.setPathParameters(map);
		val response = getURL.handleRequest(input);
		assertEquals(Integer.valueOf(400), response.getStatusCode());
		assertEquals("Invalid URL", response.getBody());
	}
	
	@Test
	public void test2() throws MalformedURLException, InvalidArgumentException {
		String shortPath="HSFSF";
		URL url = new URL("http://www.google.com");
		when(svc.getURL(shortPath, null)).thenReturn(Optional.of(url));
		val input = new APIGatewayProxyRequestEvent();
		Map<String, String> map = new HashMap<>();
		map.put("code", shortPath);
		input.setPathParameters(map);
		val response = getURL.handleRequest(input);
		assertEquals(Integer.valueOf(200), response.getStatusCode());
		assertEquals("http://www.google.com", response.getBody());
	}
	
	@Test
	public void test3() throws MalformedURLException, InvalidArgumentException {
		String shortPath="HSFSF";
		when(svc.getURL(shortPath, null)).thenReturn(Optional.empty());
		val input = new APIGatewayProxyRequestEvent();
		Map<String, String> map = new HashMap<>();
		map.put("code", shortPath);
		input.setPathParameters(map);
		val response = getURL.handleRequest(input);
		assertEquals(Integer.valueOf(404), response.getStatusCode());
		assertEquals("URL not found", response.getBody());
	}

}
