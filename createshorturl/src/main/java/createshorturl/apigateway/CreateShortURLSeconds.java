package createshorturl.apigateway;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.validator.routines.UrlValidator;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import createshorturl.services.Service;
import lombok.val;

import static shorturls.apigateway.ResponseCreator.getBadRequestResponse;
import static shorturls.apigateway.ResponseCreator.getInternalErrorResponse;
import static shorturls.apigateway.ResponseCreator.getWillBeCreatedResponse;
import shorturls.exceptions.InvalidArgumentException;
import shorturls.exceptions.ShortURLRuntimeException;

/**
 * Class for handling creation of short URLs that expire 
 * Input body is a json like the following : {"url":"http://www.google.com","seconds":"3600"}
 *
 */
public class CreateShortURLSeconds {

	private final Service service;
	private final ObjectMapper mapper = new ObjectMapper();
	private final UrlValidator urlValidator = UrlValidator.getInstance();

	public CreateShortURLSeconds(Service service) {
		this.service = service;
	}

	public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input) {
		val body = input.getBody();
		try {
			val urlExpire = mapper.readValue(body, URLExpire.class);
			val url = urlExpire.getUrl();
			val seconds = urlExpire.getSeconds();
			if (seconds > 0) {
				if (urlValidator.isValid(url)) {
					val newURL = new URL(url);
					val shortURL = service.createShortURL(newURL, urlExpire.getSeconds());
					return getWillBeCreatedResponse(shortURL.toString());
				}
				return getBadRequestResponse();
			}
		} catch (JsonProcessingException|MalformedURLException | InvalidArgumentException e) {
			return getBadRequestResponse();
		} catch (ShortURLRuntimeException e) {
			return getInternalErrorResponse();
		}
		return getBadRequestResponse();
	}

}
