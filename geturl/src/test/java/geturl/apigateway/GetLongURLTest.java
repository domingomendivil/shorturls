package geturl.apigateway;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
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
public class GetLongURLTest {
	
	
	@InjectMocks
	private GetLongURL getLongURL;
	
	@Mock
	private Service svc;

    @Test
	public void test1() throws MalformedURLException, InvalidArgumentException {
		String url = "file://wa";
		InvalidArgumentException e = new InvalidArgumentException("Invalid URL");
		APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
		Map<String, String> map = new HashMap<>();
        map.put("shortURL", url);
        input.setQueryStringParameters(map);
		var response = getLongURL.handleRequest(input);
		assertEquals(Integer.valueOf(400), response.getStatusCode());
		assertEquals("Invalid URL", response.getBody());
	}


    @Test
	public void test2() throws MalformedURLException, InvalidArgumentException {
		String url = "http://me.li/01230";
		String encoded  = encode(url);
        var returnURLStr = "http://www.google.com";
        var returnURL = new URL(returnURLStr);
        when(svc.getLongURL(new URL(url),null)).thenReturn(Optional.of(returnURL));
		APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
		Map<String, String> map = new HashMap<>();
        map.put("shortURL", encoded);
        input.setQueryStringParameters(map);
		var response = getLongURL.handleRequest(input);
		assertEquals(Integer.valueOf(200), response.getStatusCode());
        assertEquals(returnURLStr, response.getBody());
	}

    @Test
	public void test3() throws MalformedURLException, InvalidArgumentException {
		String url = "a";
		APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
		Map<String, String> map = new HashMap<>();
        map.put("shortURL", url);
        input.setQueryStringParameters(map);
		var response = getLongURL.handleRequest(input);
		assertEquals(Integer.valueOf(400), response.getStatusCode());
    }

	@Test
	public void test4() throws MalformedURLException, InvalidArgumentException {
		String url = "http://me.li/2342";
		String encoded = encode(url);
		when(svc.getLongURL(new URL(url),null)).thenReturn(Optional.empty());
		val input = new APIGatewayProxyRequestEvent();
		Map<String, String> map = new HashMap<>();
        map.put("shortURL", encoded);
        input.setQueryStringParameters(map);
		var response = getLongURL.handleRequest(input);
		assertEquals(Integer.valueOf(404), response.getStatusCode());
    	assertEquals("URL not found", response.getBody());
    
	}

    public static String encode(String raw) {
        return Base64.getUrlEncoder()
                // .withPadding()
                .encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }
}
