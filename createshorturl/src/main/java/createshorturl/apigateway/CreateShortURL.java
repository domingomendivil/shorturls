package createshorturl.apigateway;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.validator.routines.UrlValidator;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import createshorturl.services.InvalidArgumentsException;
import createshorturl.services.Service;
import createshorturl.services.ServiceException;
import static shorturls.apigateway.ResponseCreator.getBadRequestResponse;
import static shorturls.apigateway.ResponseCreator.getInternalErrorResponse;
import static shorturls.apigateway.ResponseCreator.getWillBeCreatedResponse;

public class CreateShortURL {

	private Service service;

	public CreateShortURL(Service service) {
		this.service = service;
	}

	public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input) {
		String longURL = input.getBody();
		boolean isValid = UrlValidator.getInstance().isValid(longURL);
		if (isValid) {
			try {
				var newURL = new URL(longURL);
				var shortURL = service.createShortURL(newURL);
				return getWillBeCreatedResponse(shortURL.toString());
			}  catch (MalformedURLException| InvalidArgumentsException e) {
				return getBadRequestResponse();
			} catch (ServiceException e) {
				return getInternalErrorResponse();
			}
		} else {
			return getBadRequestResponse();
		}
	}

}
