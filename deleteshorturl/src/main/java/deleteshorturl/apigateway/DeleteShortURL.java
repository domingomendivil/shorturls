package deleteshorturl.apigateway;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.validator.routines.UrlValidator;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import deleteshorturl.services.InvalidArgumentsException;
import deleteshorturl.services.Service;
import lombok.val;
import static shorturls.apigateway.ResponseCreator.getOKResponse;
import static shorturls.apigateway.ResponseCreator.getNotFoundResponse;
import static shorturls.apigateway.ResponseCreator.getBadRequestResponse;


public class DeleteShortURL {

	private static final UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
	
	
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
                }else{
                    return getNotFoundResponse();
                }
            } catch (InvalidArgumentsException|MalformedURLException e) {
                return getBadRequestResponse();
            }

        } else {
            return getBadRequestResponse();
        }
    }
}
