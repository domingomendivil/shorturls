package createshorturl.apigateway;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import createshorturl.services.Service;
import lombok.val;
import shorturls.exceptions.InvalidArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class CreateShortURLTest {
	
	@Mock
	private Service svc;

	@InjectMocks
	private CreateShortURL createShortURL;

	private void setPlainText(APIGatewayProxyRequestEvent input){
		Map<String,String> headers = new HashMap<>();
		headers.put(CreateShortURL.CONTENT_TYPE,"text/plain");
		input.setHeaders(headers);
	}

	private void setJson(APIGatewayProxyRequestEvent input){
		Map<String,String> headers = new HashMap<>();
		headers.put(CreateShortURL.CONTENT_TYPE,"application/json");
		input.setHeaders(headers);
	}
	
	private APIGatewayProxyResponseEvent handleURL(String url) {
		APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
		input.setBody(url);
		setPlainText(input);
		return createShortURL.handleRequest(input);
	}
	
	private APIGatewayProxyResponseEvent handleJson(String json) {
		APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
		input.setBody(json);
		setJson(input);
		return createShortURL.handleRequest(input);
	}
	
	private void assertBadRequest( APIGatewayProxyResponseEvent response) {
		assertEquals(Integer.valueOf(400), response.getStatusCode());
	}


	@Test
	public void test1() throws MalformedURLException, InvalidArgumentException {
		String url = "http://www.montevideo.com.uy";
		String shortURL ="http://me.li/XDFUI";
		URL returnURL = new URL(shortURL);
		when(svc.createShortURL(new URL(url))).thenReturn(returnURL);
		val response = handleURL(url);
		assertEquals(Integer.valueOf(201), response.getStatusCode());
		assertEquals(shortURL, response.getBody());
	}
	
	@ParameterizedTest
	@ValueSource(strings= {
			"file://wa",
			"ftp://site.edu.uy",
			"",
			"http:/a",
			"http://",
			"http://asdf"
	})
	public void testBadURLs(String url) throws MalformedURLException, InvalidArgumentException {
		val response = handleURL(url);
		assertBadRequest(response);
	}

	@Test
	public void test3() throws MalformedURLException, InvalidArgumentException {
		String json = "{\"url\": \"http://www.montevideo.com.uy\", \"seconds\": \"3\"}";
		String url = "http://www.montevideo.com.uy";
		String shortURL ="http://me.li/XDFUI";
		URL returnURL = new URL(shortURL);
		when(svc.createShortURL(new URL(url),3L)).thenReturn(returnURL);
		var response = handleJson(json);
		assertEquals(Integer.valueOf(201), response.getStatusCode());
		assertEquals(shortURL, response.getBody());
	}
	
	@ParameterizedTest
	@ValueSource(strings= {
			"{\"url\": \"http://www.montevideo.com.uy\", \"seconds\": \"A\"}",
			"{\"url\": \"http://www.montevideo.com.uy\", \"seconds\": \"-1\"}",
			"{\"url\": \"http://www.montevideo.com.uy\", \"seconds\": \"0\"}",
			"{\"url\": \"file://a\", \"seconds\": \"1\"}",
			"{\"url\": \"http://www.montevideo.com.uy\", \"seconds\": \"1\",\"seconds2\": \"1\"}",
			"file://site.edu.uy",
			"{\"url\": \"file://site.edu.uy\", \"seconds\": \"1\",\"seconds\": \"1\"}",
			"",
			"{\"url\": \"\", \"seconds\": \"\"}",

	})
	public void test4(String url) throws MalformedURLException, InvalidArgumentException {
		var input = new APIGatewayProxyRequestEvent();
		input.setBody(url);
		setJson(input);
		var response = createShortURL.handleRequest(input);
		assertBadRequest(response);
	}
	
	
	@Test
	public void test11() throws MalformedURLException, InvalidArgumentException {
		String json = "{\"url\": \"http://www.montevideo.com.uy\", \"seconds\": \"1\",\"seconds2\": \"1\"}";
		var input = new APIGatewayProxyRequestEvent();
		input.setBody(json);
		Map<String,String> headers = new HashMap<>();
		headers.put(CreateShortURL.CONTENT_TYPE,"text/html");
		input.setHeaders(headers);
		val response = createShortURL.handleRequest(input);
		assertBadRequest(response);
	}

	
	

}
