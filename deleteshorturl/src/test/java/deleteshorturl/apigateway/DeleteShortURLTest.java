package deleteshorturl.apigateway;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import deleteshorturl.services.InvalidArgumentsException;
import deleteshorturl.services.Service;
@RunWith(MockitoJUnitRunner.class)
public class DeleteShortURLTest {

    @Mock
    private Service svc;

    @InjectMocks
    private DeleteShortURL deleteShortURL;

    @Test
    public void test1() throws MalformedURLException, InvalidArgumentsException{
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        InvalidArgumentsException e= new InvalidArgumentsException();
        doThrow(e).when(svc).deleteURL(new URL("http://localhost:3000/"));
        var event = deleteShortURL.handleRequest(input);
        assertEquals(Integer.valueOf(400),event.getStatusCode());
        assertEquals("Invalid URL",event.getBody());
    }
    
    @Test
    public void test2() throws MalformedURLException, InvalidArgumentsException{
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        input.setBody("http://localhost:3000");
        Mockito.when(svc.deleteURL(new URL("http://localhost:3000"))).thenReturn(true);
        var event = deleteShortURL.handleRequest(input);
    
        assertEquals(Integer.valueOf(200),event.getStatusCode());
    }
    
    @Test
    public void test3() throws MalformedURLException, InvalidArgumentsException{
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        input.setBody("a");
        var event = deleteShortURL.handleRequest(input);
        assertEquals(Integer.valueOf(400),event.getStatusCode());
        assertEquals("Invalid URL",event.getBody());
    }

    @Test
    public void test4() throws MalformedURLException, InvalidArgumentsException{
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        input.setBody("");
        var event = deleteShortURL.handleRequest(input);
        assertEquals(Integer.valueOf(400),event.getStatusCode());
        assertEquals("Invalid URL",event.getBody());
    }

    @Test
    public void test5() throws MalformedURLException, InvalidArgumentsException{
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        var event = deleteShortURL.handleRequest(input);
        assertEquals(Integer.valueOf(400),event.getStatusCode());
        assertEquals("Invalid URL",event.getBody());
    }

    
}
