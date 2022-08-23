package shorturls.apigateway;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import lombok.val;

/**
 * Class for Unit Testing ResponseCreator class.
 * 
 *
 */
public class ResponseCreatorTest {

	private void assertStatusCode(int statusCode,APIGatewayProxyResponseEvent res) {
		assertEquals(Integer.valueOf(statusCode),res.getStatusCode());
	}
	
	@Test
	public void testBadRequestResponse() {
		val res= ResponseCreator.getBadRequestResponse();
		assertStatusCode(400,res);
	}
	
	@Test
	public void testInternalErrorResponse() {
		val res= ResponseCreator.getInternalErrorResponse();
		assertStatusCode(500,res);
	}
	
	@Test
	public void testMovedResponse1() {
		Map<String, String> headers = new HashMap<>();
		val res= ResponseCreator.getMovedResponse(headers,"http://www.google.com","");
		assertStatusCode(301,res);
	}
	
	@Test
	public void testGetNotFoundResponse1() {
		val res =  ResponseCreator.getNotFoundResponse();
		assertStatusCode(404,res);
	}
	
	@Test
	public void testGetURLCreatedResponse1() {
		val res =  ResponseCreator.getURLCreatedResponse("http://me.li/asx");
		assertStatusCode(202,res);
		assertEquals("http://me.li/asx",res.getBody());
	}
	
	@Test
	public void testGetOkResponse() {
		Map<String, String> headers = new HashMap<>();
		val res= ResponseCreator.getOKResponse("http://www.google.com");
		assertStatusCode(200,res);
		assertEquals("http://www.google.com",res.getBody());

	}
	
	
	
	
	
}
