package geturl.apigateway;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import geturl.services.Service;
import lombok.val;
import shorturls.apigateway.ResponseCreator;
import shorturls.exceptions.InvalidArgumentException;

/**
 * Class for handling HTTP requests for getting URLs from
 * short URLs. It makes requests validations and delegates
 * the creation to the the service layer
 *
 */
public class GetURL {

    private final Service service;

    public GetURL(Service service){
        this.service=service;
    }

    
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input) {
        val pars = input.getPathParameters();
        if (pars!=null){
            String shortURL = pars.get("code");
            try {
                val longURL = service.getURL(shortURL,input.getHeaders());
                if (longURL.isEmpty()) {
                    return ResponseCreator.getNotFoundResponse();
                }else {
                    return ResponseCreator.getOKResponse(longURL.get().toString());
                }
            } catch (InvalidArgumentException e) {
                //error in request, return next in the code
            } 
        }
        return ResponseCreator.getBadRequestResponse();
    }
}
