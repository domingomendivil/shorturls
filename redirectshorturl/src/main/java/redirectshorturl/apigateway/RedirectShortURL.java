package redirectshorturl.apigateway;

import static shorturls.apigateway.ResponseCreator.getBadRequestResponse;
import static shorturls.apigateway.ResponseCreator.getMovedResponse;
import static shorturls.apigateway.ResponseCreator.getNotFoundResponse;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import geturl.services.Service;
import lombok.val;
import shorturls.exceptions.InvalidArgumentException;

public class RedirectShortURL {
 
    private final Service service;

    public RedirectShortURL(Service service){
        this.service=service;
    }
    
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input) {
    	val map =input.getPathParameters();
        val shortPath = map.get("code");
        if (shortPath!=null && (!shortPath.equals(""))){
            try {
                val headers=input.getHeaders();
                var longURL = service.getURL(shortPath,headers);
                if (longURL.isEmpty()) {
                    return getNotFoundResponse();
                } else {

                    return getMovedResponse(headers,longURL.get().toString());
                }
            } catch (InvalidArgumentException e) {
            	//bad request, return next in code
            }
        }
        return getBadRequestResponse();
    }
}
