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
import shorturls.config.ShortURLProperties;
import shorturls.exceptions.InvalidArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class CreateShortURLTest {
	
	@Mock
	private Service svc;
	
	@Mock
	private ShortURLProperties props;

	@InjectMocks
	private CreateShortURL createShortURL;
	
	private static final String CONTENT_TYPE="content-type";
	
	private void assertBody(String body,APIGatewayProxyResponseEvent response) {
		assertEquals(body, response.getBody());
	}

	
	private APIGatewayProxyResponseEvent handle(String body,String contentType) {
		APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
		input.setBody(body);
		Map<String,String> headers = new HashMap<>();
		headers.put(CONTENT_TYPE,contentType);
		input.setHeaders(headers);
		return createShortURL.handleRequest(input);
	}
	
	private APIGatewayProxyResponseEvent handleURL(String url) {
		return handle(url,"text/plain");
	}
	
	private APIGatewayProxyResponseEvent handleJson(String json) {
		return handle(json,"application/json");
	}
	
	private void assertBadRequest( APIGatewayProxyResponseEvent response) {
		assertResponse(400,response);
	}
	
	private void assertResponse(int statusCode,APIGatewayProxyResponseEvent response) {
		assertEquals(Integer.valueOf(statusCode), response.getStatusCode());
	}
	
	private void whenServiceInvokeReturn(String url,Long seconds,String returnedURL) throws MalformedURLException, InvalidArgumentException {
		URL response = new URL(returnedURL);
		if (seconds == null)
			when(svc.createShortURL(new URL(url))).thenReturn(response);
		else
		    when(svc.createShortURL(new URL(url),seconds)).thenReturn(response);
	}
	

	private void setContentType(){
		when(props.getProperty("CONTENT_TYPE")).thenReturn(CONTENT_TYPE);
	}

	private void assertOKResponse(String shortURL,APIGatewayProxyResponseEvent response){
		assertResponse(201,response);
		assertBody(shortURL, response);
	}


	@Test
	public void test1() throws MalformedURLException, InvalidArgumentException {
		setContentType();
		String url = "http://www.montevideo.com.uy";
		String shortURL ="http://me.li/XDFUI";
		whenServiceInvokeReturn(url,null,shortURL);
		val response = handleURL(url);
		assertOKResponse(shortURL, response);
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
		setContentType();
		String json = "{\"url\": \"http://www.montevideo.com.uy\", \"seconds\": \"3\"}";
		String url = "http://www.montevideo.com.uy";
		String shortURL ="http://me.li/XDFUI";
		whenServiceInvokeReturn(url, 3L, shortURL);
		var response = handleJson(json);
		assertOKResponse(shortURL, response);
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
		val response = handleJson(url);
		assertBadRequest(response);
	}
	
	
	@Test
	public void test5() throws MalformedURLException, InvalidArgumentException {
		setContentType();
		String json = "{\"url\": \"http://www.montevideo.com.uy\", \"seconds\": \"1\",\"seconds2\": \"1\"}";
		val response = handle(json,"text/html");
		assertBadRequest(response);
	}
	
	@Test
	public void test6() throws MalformedURLException, InvalidArgumentException {
		setContentType();
		String json = "http://www.ladiaria.com.uy";
		String shortURL ="http://me.li/XDFUI";
		whenServiceInvokeReturn(json,null,shortURL);
		val response = handle(json,"text/html");
		assertOKResponse(shortURL, response);
	}

	
	

}
