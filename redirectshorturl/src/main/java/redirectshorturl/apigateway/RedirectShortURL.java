package redirectshorturl.apigateway;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import geturl.services.InvalidArgumentsException;
import geturl.services.Service;
import static shorturls.apigateway.ResponseCreator.getNotFoundResponse;
import static shorturls.apigateway.ResponseCreator.getMovedResponse;
import static shorturls.apigateway.ResponseCreator.getBadRequestResponse;

public class RedirectShortURL {
 
    private final Service service;

    public RedirectShortURL(Service service){
        this.service=service;
    }
    
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input) {
    	var map =input.getPathParameters();
        var shortPath = map.get("code");
        if (shortPath!=null && (!shortPath.equals(""))){
            try {
                var longURL = service.getLongURL(shortPath,input.getHeaders());
                if (longURL.isEmpty()) {
                    return getNotFoundResponse();
                } else {
                    return getMovedResponse(longURL.get().toString());
                }
            } catch (InvalidArgumentsException e) {
                return getBadRequestResponse();
            }
        }
        return getBadRequestResponse();
    }
}
