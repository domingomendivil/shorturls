package createshorturl.apigateway;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import createshorturl.services.Service;
import shorturls.exceptions.InvalidArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class CreateShortURLSecondsTest {
	
	@Mock
	private Service svc;

	@InjectMocks
	private CreateShortURLSeconds createShortURLHours;

	@Test
	public void test1() throws MalformedURLException, InvalidArgumentException {
		String json = "{\"url\": \"http://www.montevideo.com.uy\", \"seconds\": \"3\"}";
		String url = "http://www.montevideo.com.uy";
		String shortURL ="http://me.li/XDFUI";
		URL returnURL = new URL(shortURL);
		when(svc.createShortURL(new URL(url),3L)).thenReturn(returnURL);
		var response = handleRequest(json);
		assertEquals(Integer.valueOf(201), response.getStatusCode());
		assertEquals(shortURL, response.getBody());
	}
	
	
	@Test
	public void test2() throws MalformedURLException, InvalidArgumentException {
		String url = "file://wa";
		var response = handleRequest(url);
		assertEquals(Integer.valueOf(400), response.getStatusCode());
	}
	
	@Test
	public void test3() throws MalformedURLException, InvalidArgumentException {
		String url = "http://www.montevideo.com.uy";
		var response = handleRequest(url);
		assertEquals(Integer.valueOf(400), response.getStatusCode());
	}

	@ParameterizedTest
	@ValueSource(strings= {
			"{\"url\": \"http://www.montevideo.com.uy\", \"seconds\": \"A\"}",
			"{\"url\": \"http://www.montevideo.com.uy\", \"seconds\": \"-1\"}",
			"{\"url\": \"http://www.montevideo.com.uy\", \"seconds\": \"0\"}",
			"{\"url\": \"file://a\", \"seconds\": \"1\"}",
			"{\"url\": \"http://www.montevideo.com.uy\", \"seconds\": \"1\",\"seconds2\": \"1\"}"
	})
	public void test5(String arg) throws MalformedURLException, InvalidArgumentException {
		var response = handleRequest(arg);
		assertEquals(Integer.valueOf(400), response.getStatusCode());
	
	}

	private APIGatewayProxyResponseEvent handleRequest(String body) {
		var input = new APIGatewayProxyRequestEvent();
		input.setBody(body);
		return createShortURLHours.handleRequest(input);
	}


}
