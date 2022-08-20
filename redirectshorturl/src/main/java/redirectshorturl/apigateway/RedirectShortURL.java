package redirectshorturl.apigateway;

import static shorturls.apigateway.ResponseCreator.getBadRequestResponse;
import static shorturls.apigateway.ResponseCreator.getMovedResponse;
import static shorturls.apigateway.ResponseCreator.getNotFoundResponse;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import geturl.services.Service;
import lombok.val;
import shorturls.config.ShortURLProperties;
import shorturls.exceptions.InvalidArgumentException;

/**
 * ApiGateway class for redirecting short URLs to the original URL.
 * It was separated in a standalone class that can be unit tested.
 *
 */
public class RedirectShortURL {
 
	/*
	 * Service layer responsible for getting the original URL
	 */
    private final Service service;

    private ShortURLProperties props;

    /*
     * Constructor, where the service layer is injected
     */
    public RedirectShortURL(Service service,ShortURLProperties props){
        this.service=service;
        this.props=props;
    }
    
    /*
     * Method responsible for handling http requests. It validates the http request
     * and if it's valid, it extracts the short url, which is passed as parameter
     * to the getURL method of the service layer.
     * In case the original URL is found, it is returned in the Location header
     * with a redirect status code. Otherwise, an HTTP 404 is returned.
     */
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input) {
    	System.out.println("apigateway handlerequest");
        val map =input.getPathParameters();
        val shortPath = map.get("code");
        if (shortPath!=null && (!shortPath.equals(""))){
            System.out.println("apigateway path valida");
            try {
                val headers=input.getHeaders();
                System.out.println("antes de service.getURL");
                var longURL = service.getURL(shortPath,headers);
                System.out.println("luego de service.getURL");
                if (longURL.isEmpty()) {
                    return getNotFoundResponse();
                } else {
                    var cookieConfig = props.getProperty("COOKIE_CONFIG");
                   System.out.println("cookieConfig "+cookieConfig);
                    if (cookieConfig==null)
                        cookieConfig="";
                    return getMovedResponse(headers,longURL.get().toString(),cookieConfig);
                }
            } catch (InvalidArgumentException e) {
                System.out.println("error");
            	e.printStackTrace();
            }
        }
        return getBadRequestResponse();
    }
}
