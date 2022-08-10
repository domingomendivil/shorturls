package createshorturl.apigateway;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import createshorturl.services.Service;
import shorturls.exceptions.InvalidArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class CreateShortURLTest {
	
	@Mock
	private Service svc;

	@InjectMocks
	private CreateShortURL createShortURL;

	@Test
	public void test1() throws MalformedURLException, InvalidArgumentException {
		String url = "http://www.montevideo.com.uy";
		String shortURL ="http://me.li/XDFUI";
		URL returnURL = new URL(shortURL);
		when(svc.createShortURL(new URL(url))).thenReturn(returnURL);
		APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
		input.setBody(url);
		var response = createShortURL.handleRequest(input);
		assertEquals(Integer.valueOf(201), response.getStatusCode());
		assertEquals(shortURL, response.getBody());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test2() throws MalformedURLException, InvalidArgumentException {
		String url = "file://wa";
		APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
		input.setBody(url);
		var response = createShortURL.handleRequest(input);
		assertEquals(Integer.valueOf(400), response.getStatusCode());
	}
	
	

}
