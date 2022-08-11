package geturl.apigateway;

import static shorturls.apigateway.ResponseCreator.getBadRequestResponse;
import static shorturls.apigateway.ResponseCreator.getNotFoundResponse;
import static shorturls.apigateway.ResponseCreator.getOKResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.apache.commons.validator.routines.UrlValidator;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import geturl.services.Service;
import lombok.val;
import shorturls.exceptions.InvalidArgumentException;

public class GetLongURL {

    private static final UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);

    private final Service service;
    
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

    public String decode(String raw) {
        byte[] bytes = decoder.decode(raw);
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
