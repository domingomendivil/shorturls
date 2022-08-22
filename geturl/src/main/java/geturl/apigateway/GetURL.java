package geturl.apigateway;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import geturl.services.Service;
import lombok.val;
import static shorturls.apigateway.ResponseCreator.getNotFoundResponse;
import static shorturls.apigateway.ResponseCreator.getOKResponse;
import static shorturls.apigateway.ResponseCreator.getBadRequestResponse;
import shorturls.exceptions.InvalidArgumentException;

/**
 * Class for handling HTTP requests for getting URLs from
 * short URLs. It makes requests validations and delegates
 * the creation to the the service layer
 *
 */
public class GetURL {
    /**
     * Service layer
     */
    private final Service service;

    public GetURL(Service service){
        this.service=service;
    }

    /**
     * Method for handling HTTP Requests for getting original URLs
     * associated with a short URL
     * @param input an APIGatewayProxyRequestEvent with the HTTP Request data
     * @return an APIGatewayProxyResponseEvent
     */
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input) {
        val pars = input.getPathParameters();
        if (pars!=null){
            val shortURL = pars.get("code");
            try {
                val longURL = service.getURL(shortURL,input.getHeaders());
                if (longURL.isEmpty()) {
                    return getNotFoundResponse();
                }else {
                    return getOKResponse(longURL.get().toString());
                }
            } catch (InvalidArgumentException e) {
                //error in request, return next in the code
            } 
        }
        return getBadRequestResponse();
    }
}
