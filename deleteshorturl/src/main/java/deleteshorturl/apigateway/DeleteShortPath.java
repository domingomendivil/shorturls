package deleteshorturl.apigateway;

import java.net.MalformedURLException;
import java.net.URL;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import deleteshorturl.services.InvalidArgumentsException;
import deleteshorturl.services.Service;
import lombok.val;
import static shorturls.apigateway.ResponseCreator.getOKResponse;
import static shorturls.apigateway.ResponseCreator.getNotFoundResponse;
import static shorturls.apigateway.ResponseCreator.getBadRequestResponse;

public class DeleteShortPath {

    private Service service;

    public DeleteShortPath(Service service) {
        this.service=service;
    }

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input) {
        val pars = input.getPathParameters();
        if (pars!=null){
            String shortPath = pars.get("code");
            try {
                if (service.deleteURL(shortPath)){
                    return getOKResponse("URL deleted");
                }else{
                    return getNotFoundResponse();
                }
            } catch (InvalidArgumentsException e) {
                return getBadRequestResponse();
            }
        }else{
            return getBadRequestResponse();
        }
    }
    
}
