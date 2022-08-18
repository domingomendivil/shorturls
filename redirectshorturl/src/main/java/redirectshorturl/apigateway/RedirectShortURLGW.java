package redirectshorturl.apigateway;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import redirectshorturl.config.Factory;

/**
 * AWS Lambda Handler request for redirecting to a long URL given the short URL
 */
public class RedirectShortURLGW implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final RedirectShortURL service = Factory.getRedirectShortURL();

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    	return service.handleRequest(input);
    }

}
