package deleteshorturl.apigateway;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import deleteshorturl.config.DeleteFactory;

/**
 * AWS Lambda Handler request for deleting a Short URL
 */
public class DeleteShortPathGW implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final DeleteShortPath service = DeleteFactory.getDeleteShortPath();

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    	return service.handleRequest(input);
    }

}
