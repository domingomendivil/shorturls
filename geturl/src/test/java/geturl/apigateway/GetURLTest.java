package geturl.apigateway;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import geturl.services.InvalidArgumentsException;
import geturl.services.Service;

@RunWith(MockitoJUnitRunner.class)
public class GetURLTest {

	@InjectMocks
	private GetURL getURL;

	@Mock
	private Service svc;

	@Test
	public void test1() throws MalformedURLException, InvalidArgumentsException {
		String shortPath="HSFSF";
		InvalidArgumentsException e = new InvalidArgumentsException();
		when(svc.getURL(shortPath, null)).thenThrow(e);
		APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
		Map<String, String> map = new HashMap<>();
		map.put("code", shortPath);
		input.setPathParameters(map);
		var response = getURL.handleRequest(input);
		assertEquals(Integer.valueOf(400), response.getStatusCode());
	}

}
