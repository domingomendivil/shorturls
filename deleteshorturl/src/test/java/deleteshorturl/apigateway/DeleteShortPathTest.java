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
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import deleteshorturl.services.Service;
import lombok.val;
import shorturls.exceptions.InvalidArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class DeleteShortPathTest {
    
    @Mock
    private Service svc;

    @InjectMocks
    private DeleteShortPath deleteShortPath;
    
    
    private void assertBadRequest(APIGatewayProxyResponseEvent res) {
    	assertResponse(400, res);
    }
    
    private void assertResponse(int code,APIGatewayProxyResponseEvent res) {
    	assertEquals(Integer.valueOf(code), res.getStatusCode());
    }
    
    
    @Test
    public void test1() {
    	val input = new APIGatewayProxyRequestEvent();
    	val res = deleteShortPath.handleRequest(input);
    	assertBadRequest(res);
    }
    
    @Test
    public void test2() {
    	val input = getInputParameters(null);
    	var res = deleteShortPath.handleRequest(input);
    	assertBadRequest(res);
    }
    
    @Test
    public void test3() throws InvalidArgumentException{
    	val input = getInputParameters("nvasdkh");
		when(svc.deleteURL("nvasdkh")).thenReturn(true);
    	var res = deleteShortPath.handleRequest(input);
    	assertResponse(200, res);
    }
    
    @Test
    public void test4() throws InvalidArgumentException{
		val input = getInputParameters("nvasdkh");
		when(svc.deleteURL("nvasdkh")).thenReturn(false);
    	var res = deleteShortPath.handleRequest(input);
    	assertResponse(404, res);
    }
    
    private APIGatewayProxyRequestEvent getInputParameters(String shortPath) {
    	val input = new APIGatewayProxyRequestEvent();
    	val pars = new HashMap<String,String>();
    	pars.put("code", shortPath);
    	input.setPathParameters(pars);
    	return input;
    }
    
    
    
    
}
