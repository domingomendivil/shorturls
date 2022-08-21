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
import lombok.val;
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

    private APIGatewayProxyResponseEvent deleteShortURL(String shortURL){
        val input = new APIGatewayProxyRequestEvent();
        input.setBody(shortURL);
        return deleteShortURL.handleRequest(input);
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
        when(svc.deleteURL(new URL("http://localhost:3000"))).thenReturn(true);
        val event=deleteShortURL("http://localhost:3000");
        assertEquals(Integer.valueOf(200),event.getStatusCode());
    }
    
    @Test
    public void test3() throws MalformedURLException, InvalidArgumentException{
        val event=deleteShortURL("a");
        assertBadRequest(event);
        assertEquals("Invalid URL",event.getBody());
    }

    @Test
    public void test4() throws MalformedURLException, InvalidArgumentException{
        val event = deleteShortURL("");
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
        when(svc.deleteURL(new URL("http://localhost:3000/asdf"))).thenReturn(false);
        val event = deleteShortURL("http://localhost:3000/asdf");
        assertEquals(Integer.valueOf(404),event.getStatusCode());
    }

    
}
