package deleteshorturl.apigateway;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import deleteshorturl.services.Service;
import lombok.val;
import shorturls.exceptions.InvalidArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class DeleteShortPathTest {
    
    @Mock
    private Service svc;

    @InjectMocks
    private DeleteShortPath deleteShortPath;
    
    
    @Test
    public void test1() {
    	val input = new APIGatewayProxyRequestEvent();
    	val res = deleteShortPath.handleRequest(input);
    	assertEquals(Integer.valueOf(400), res.getStatusCode());
    }
    
    @Test
    public void test2() {
    	val input = new APIGatewayProxyRequestEvent();
    	val pars = new HashMap<String,String>();
    	pars.put("code", null);
		input.setPathParameters(pars);
    	var res = deleteShortPath.handleRequest(input);
    	assertEquals(Integer.valueOf(400), res.getStatusCode());
    }
    
    @Test
    public void test3() throws InvalidArgumentException{
    	val input = new APIGatewayProxyRequestEvent();
    	val pars = new HashMap<String,String>();
    	pars.put("code", "nvasdkh");
		input.setPathParameters(pars);
		when(svc.deleteURL("nvasdkh")).thenReturn(true);
    	var res = deleteShortPath.handleRequest(input);
    	assertEquals(Integer.valueOf(200), res.getStatusCode());
    }
    
    @Test
    public void test4() throws InvalidArgumentException{
    	val input = new APIGatewayProxyRequestEvent();
    	val pars = new HashMap<String,String>();
    	pars.put("code", "nvasdkh");
		input.setPathParameters(pars);
		when(svc.deleteURL("nvasdkh")).thenReturn(false);
    	var res = deleteShortPath.handleRequest(input);
    	assertEquals(Integer.valueOf(404), res.getStatusCode());
    }
    
    
    
    
}
