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
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import geturl.services.Service;
import lombok.val;
import shorturls.exceptions.InvalidArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class GetURLTest {

	@InjectMocks
	private GetURL getURL;

	@Mock
	private Service svc;

	
	private void assertResponse(int code,APIGatewayProxyResponseEvent response) {
		assertEquals(Integer.valueOf(code), response.getStatusCode());
	}
	
	private void assertBody(String body,APIGatewayProxyResponseEvent response) {
		assertEquals(body, response.getBody());
	}
	
	private void whenInvokeServiceReturn(String shortPath,Optional<URL> optional) throws InvalidArgumentException {
		when(svc.getURL(shortPath, null)).thenReturn(optional);
	}
	
	private APIGatewayProxyResponseEvent getURL(String code) {
		val input = new APIGatewayProxyRequestEvent();
		Map<String, String> map = new HashMap<>();
		map.put("code", code);
		input.setPathParameters(map);
		return getURL.handleRequest(input);
	}
	
	
	@Test
	public void test1() throws MalformedURLException, InvalidArgumentException {
		val shortPath="HSFSF";
		val e = new InvalidArgumentException("Invalid URL");
		when(svc.getURL(shortPath, null)).thenThrow(e);
		val response = getURL(shortPath);
		assertResponse(400,response);
		assertBody("Invalid URL", response);
	}
	
	@Test
	public void test2() throws MalformedURLException, InvalidArgumentException {
		String shortPath="HSFSF";
		URL url = new URL("http://www.google.com");
		whenInvokeServiceReturn(shortPath,Optional.of(url));
		val response = getURL(shortPath);
		assertResponse(200,response);
		assertBody("http://www.google.com", response);
	}
	
	@Test
	public void test3() throws MalformedURLException, InvalidArgumentException {
		String shortPath="HSFSF";
		whenInvokeServiceReturn(shortPath,Optional.empty());
		val response = getURL(shortPath);
		assertResponse(404,response);
		assertBody("URL not found", response);
	}

}
