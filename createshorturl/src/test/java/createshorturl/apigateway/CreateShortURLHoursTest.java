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
public class CreateShortURLHoursTest {
	
	@Mock
	private Service svc;

	@InjectMocks
	private CreateShortURLHours createShortURLHours;

	@Test
	public void test1() throws MalformedURLException, InvalidArgumentException {
		String json = "{\"url\": \"http://www.montevideo.com.uy\", \"hours\": \"3\"}";
		String url = "http://www.montevideo.com.uy";
		String shortURL ="http://me.li/XDFUI";
		URL returnURL = new URL(shortURL);
		when(svc.createShortURL(new URL(url),3L)).thenReturn(returnURL);
		var input = new APIGatewayProxyRequestEvent();
		input.setBody(json);
		var response = createShortURLHours.handleRequest(input);
		assertEquals(Integer.valueOf(201), response.getStatusCode());
		assertEquals(shortURL, response.getBody());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test2() throws MalformedURLException, InvalidArgumentException {
		String url = "file://wa";
		var input = new APIGatewayProxyRequestEvent();
		input.setBody(url);
		var response = createShortURLHours.handleRequest(input);
		assertEquals(Integer.valueOf(400), response.getStatusCode());
	}
	
	@Test
	public void test3() throws MalformedURLException, InvalidArgumentException {
		String url = "http://www.montevideo.com.uy";
		var input = new APIGatewayProxyRequestEvent();
		input.setBody(url);
		var response = createShortURLHours.handleRequest(input);
		assertEquals(Integer.valueOf(400), response.getStatusCode());
	}

	@Test
	public void test5() throws MalformedURLException, InvalidArgumentException {
		String json = "{\"url\": \"http://www.montevideo.com.uy\", \"hours\": \"A\"}";
		var input = new APIGatewayProxyRequestEvent();
		input.setBody(json);
		var response = createShortURLHours.handleRequest(input);
		assertEquals(Integer.valueOf(400), response.getStatusCode());
	
	}
	@Test
	public void test6() throws MalformedURLException, InvalidArgumentException {
		String json = "{\"url\": \"http://www.montevideo.com.uy\", \"hours\": \"-1\"}";
		var input = new APIGatewayProxyRequestEvent();
		input.setBody(json);
		var response = createShortURLHours.handleRequest(input);
		assertEquals(Integer.valueOf(400), response.getStatusCode());
	}

	@Test
	public void test7() throws MalformedURLException, InvalidArgumentException {
		String json = "{\"url\": \"http://www.montevideo.com.uy\", \"hours\": \"0\"}";
		var input = new APIGatewayProxyRequestEvent();
		input.setBody(json);
		var response = createShortURLHours.handleRequest(input);
		assertEquals(Integer.valueOf(400), response.getStatusCode());
	}


	@Test
	public void test8() throws MalformedURLException, InvalidArgumentException {
		String json = "{\"url\": \"file://a\", \"hours\": \"1\"}";
		var input = new APIGatewayProxyRequestEvent();
		input.setBody(json);
		var response = createShortURLHours.handleRequest(input);
		assertEquals(Integer.valueOf(400), response.getStatusCode());
	}

	@Test
	public void test9() throws MalformedURLException, InvalidArgumentException {
		String json = "{\"url\": \"http://www.montevideo.com.uy\", \"hours\": \"1\",\"hours2\": \"1\"}";
		var input = new APIGatewayProxyRequestEvent();
		input.setBody(json);
		var response = createShortURLHours.handleRequest(input);
		assertEquals(Integer.valueOf(400), response.getStatusCode());
	}


}
