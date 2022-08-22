package geturl.apigateway;

import static shorturls.apigateway.ResponseCreator.getBadRequestResponse;
import static shorturls.apigateway.ResponseCreator.getNotFoundResponse;
import static shorturls.apigateway.ResponseCreator.getOKResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;

import org.apache.commons.validator.routines.UrlValidator;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import geturl.services.Service;
import lombok.val;
import shorturls.exceptions.InvalidArgumentException;

/**
 * Class responsible for handling HTTP Get URL requests.
 * It was separated as a standalone to be well unit tested.
 */
public class GetLongURL {

	/*
	 * URLValidator is used to validate that URLs are well formatted
	 */
    private static final UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);

    /*
     * Service layer that given the short URL gets the original URL 
     */
    private final Service service;
    
    /*
     * Base64 decoder used to decode URLs that come encoded in query parameters of the URL
     */
    private final Decoder decoder  = Base64.getUrlDecoder();
    
    
    public GetLongURL(Service service) {
        this.service = service;
    }

    
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input) {
        val pars = input.getQueryStringParameters();
        if (pars != null) {
            val shortURL = pars.get("shortURL");
            try {
                val shortURLDecoded = decode(shortURL);
                if (urlValidator.isValid(shortURLDecoded)) {
                    val newUrl = new URL(shortURLDecoded);
                    val longURL = service.getLongURL(newUrl, input.getHeaders());
                    if (longURL.isEmpty()) {
                        return getNotFoundResponse();
                    } else {
                        return getOKResponse(longURL.get().toString());
                    }
                }
            } catch (InvalidArgumentException | MalformedURLException| IllegalArgumentException e) {
               //error in request, return next in the code
            }
        }

        return getBadRequestResponse();
    }

    public final String decode(String raw) {
        byte[] bytes = decoder.decode(raw);
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
