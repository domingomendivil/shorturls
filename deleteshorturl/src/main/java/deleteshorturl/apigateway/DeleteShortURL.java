package deleteshorturl.apigateway;

import static shorturls.apigateway.ResponseCreator.getBadRequestResponse;
import static shorturls.apigateway.ResponseCreator.getNotFoundResponse;
import static shorturls.apigateway.ResponseCreator.getOKResponse;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.validator.routines.UrlValidator;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import deleteshorturl.services.Service;
import lombok.val;
import shorturls.exceptions.InvalidArgumentException;


/**
 * Class for handling HTTP requests for deleting short URLs.
 */
public class DeleteShortURL {

    /*
     * Validator of URLs
     */
	private final UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
	
	
    /*
     * Service Layer
     */
    private final Service service;

    public DeleteShortURL(Service service){
        this.service=service;
    }
    
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input) {
        val shortURL = input.getBody();
        val isValid = urlValidator.isValid(shortURL);
        if (isValid) {
            try {
                val shortPath = new URL(shortURL);
                if (service.deleteURL(shortPath)){
                    return getOKResponse("URL deleted");
                }else {
                	return getNotFoundResponse();
                }
            } catch (InvalidArgumentException|MalformedURLException e) {
               //bad request, return next in code
            }
        }
        return getBadRequestResponse();
    }
}
