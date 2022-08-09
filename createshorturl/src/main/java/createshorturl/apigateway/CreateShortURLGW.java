package createshorturl.apigateway;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import createshorturl.config.CreateFactory;

/**

/**
 * AWS Lambda Handler request for creating a short URL associated with a given
 * long URL
 */
public class CreateShortURLGW implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private final CreateShortURL createShortURL = CreateFactory.getCreateShortURL();

	public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
		return createShortURL.handleRequest(input);
	}
	



}
