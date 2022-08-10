package geturl.apigateway;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import geturl.services.InvalidArgumentsException;
import geturl.services.Service;
import lombok.val;
import shorturls.apigateway.ResponseCreator;

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
                var longURL = service.getURL(shortURL,input.getHeaders());
                if (longURL.isEmpty()) {
                    return ResponseCreator.getNotFoundResponse();
                }else {
                    return ResponseCreator.getOKResponse(longURL.get().toString());
                }
            } catch (InvalidArgumentsException e) {
                //error in request, return next in the code
            }

        }
        return ResponseCreator.getBadRequestResponse();
    }
}
