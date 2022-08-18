package shorturls.apigateway;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class ResponseCreatorTest {

	private void assertStatusCode(int statusCode,APIGatewayProxyResponseEvent res) {
		assertEquals(Integer.valueOf(statusCode),res.getStatusCode());
	}
	
	@Test
	public void test1() {
		APIGatewayProxyResponseEvent res= ResponseCreator.getBadRequestResponse();
		assertStatusCode(400,res);
	}
	
	@Test
	public void test2() {
		APIGatewayProxyResponseEvent res= ResponseCreator.getInternalErrorResponse();
		assertStatusCode(500,res);
	}
	
}
