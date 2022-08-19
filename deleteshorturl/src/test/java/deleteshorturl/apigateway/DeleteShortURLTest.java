package deleteshorturl.apigateway;

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
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import deleteshorturl.services.Service;
import shorturls.exceptions.InvalidArgumentException;
@RunWith(MockitoJUnitRunner.class)
public class DeleteShortURLTest {

    @Mock
    private Service svc;

    @InjectMocks
    private DeleteShortURL deleteShortURL;
    
    private void assertBadRequest(APIGatewayProxyResponseEvent res) {
    	assertResponse(400, res);
    }
    
    private void assertResponse(int code,APIGatewayProxyResponseEvent res) {
    	assertEquals(Integer.valueOf(code), res.getStatusCode());
    }

    @Test
    public void test1() throws MalformedURLException, InvalidArgumentException{
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        var event = deleteShortURL.handleRequest(input);
        assertBadRequest(event);
        assertEquals("Invalid URL",event.getBody());
    }
    
    @Test
    public void test2() throws MalformedURLException, InvalidArgumentException{
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        input.setBody("http://localhost:3000");
        when(svc.deleteURL(new URL("http://localhost:3000"))).thenReturn(true);
        var event = deleteShortURL.handleRequest(input);
    
        assertEquals(Integer.valueOf(200),event.getStatusCode());
    }
    
    @Test
    public void test3() throws MalformedURLException, InvalidArgumentException{
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        input.setBody("a");
        var event = deleteShortURL.handleRequest(input);
        assertBadRequest(event);
        assertEquals("Invalid URL",event.getBody());
    }

    @Test
    public void test4() throws MalformedURLException, InvalidArgumentException{
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        input.setBody("");
        var event = deleteShortURL.handleRequest(input);
        assertBadRequest(event);
        assertEquals("Invalid URL",event.getBody());
    }

    @Test
    public void test5() throws MalformedURLException, InvalidArgumentException{
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        var event = deleteShortURL.handleRequest(input);
        assertBadRequest(event);
        assertEquals("Invalid URL",event.getBody());
    }
    
    @Test
    public void test6() throws MalformedURLException, InvalidArgumentException{
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        input.setBody("http://localhost:3000/asdf");
        when(svc.deleteURL(new URL("http://localhost:3000/asdf"))).thenReturn(false);
        var event = deleteShortURL.handleRequest(input);
        assertEquals(Integer.valueOf(404),event.getStatusCode());
    }

    
}
