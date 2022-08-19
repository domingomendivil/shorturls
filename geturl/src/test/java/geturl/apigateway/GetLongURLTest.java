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
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import geturl.services.Service;
import lombok.val;
import shorturls.exceptions.InvalidArgumentException;


@RunWith(MockitoJUnitRunner.class)
public class GetLongURLTest {
	
	
	@InjectMocks
	private GetLongURL getLongURL;
	
	@Mock
	private Service svc;
	
	private void assertResponse(int code,APIGatewayProxyResponseEvent response) {
		assertEquals(Integer.valueOf(code), response.getStatusCode());
	}
	
	
	private void assertBody(String body,APIGatewayProxyResponseEvent response) {
		assertEquals(body, response.getBody());
	}
	
	private APIGatewayProxyResponseEvent getLongURL(String url) {
		APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
		Map<String, String> map = new HashMap<>();
        map.put("shortURL", url);
        input.setQueryStringParameters(map);
		return getLongURL.handleRequest(input);
	}

    @Test
	public void test1() throws MalformedURLException, InvalidArgumentException {
		String url = "file://wa";
		val response = getLongURL(url);
		assertResponse(400,response);
		assertBody("Invalid URL",response);
	}


    @Test
	public void test2() throws MalformedURLException, InvalidArgumentException {
		String url = "http://me.li/01230";
		String encoded  = encode(url);
        var returnURLStr = "http://www.google.com";
        var returnURL = new URL(returnURLStr);
        when(svc.getLongURL(new URL(url),null)).thenReturn(Optional.of(returnURL));
		val response = getLongURL(encoded);
		assertResponse(200,response);
		assertBody(returnURLStr,response);
 	}

    @Test
	public void test3() throws MalformedURLException, InvalidArgumentException {
		String url = "a";
		val response = getLongURL(url);
		assertResponse(400,response);

    }

	@Test
	public void test4() throws MalformedURLException, InvalidArgumentException {
		String url = "http://me.li/2342";
		String encoded = encode(url);
		when(svc.getLongURL(new URL(url),null)).thenReturn(Optional.empty());
		val response = getLongURL(encoded);
		assertResponse(404,response);
    	assertBody("URL not found",response);
    
	}

    public static String encode(String raw) {
        return Base64.getUrlEncoder()
                // .withPadding()
                .encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }
}
