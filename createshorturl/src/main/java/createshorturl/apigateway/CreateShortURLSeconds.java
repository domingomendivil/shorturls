package createshorturl.apigateway;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.validator.routines.UrlValidator;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import createshorturl.services.Service;
import createshorturl.services.ServiceException;
import shorturls.apigateway.ResponseCreator;
import shorturls.exceptions.InvalidArgumentException;

public class CreateShortURLSeconds {

	private final Service service;

	public CreateShortURLSeconds(Service service) {
		this.service = service;
	}

	public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input) {
		String body = input.getBody();
		ObjectMapper mapper = new ObjectMapper();
		URLExpire urlHours;
		try{
			urlHours=mapper.readValue(body,URLExpire.class);
		}catch(JsonProcessingException e){
			return ResponseCreator.getBadRequestResponse();
		}
			
		var url = urlHours.getUrl();
		var seconds=urlHours.getSeconds();
		if (seconds <=0)
			return ResponseCreator.getBadRequestResponse();
		boolean isValid = UrlValidator.getInstance().isValid(url);
		if (isValid) {
			try {
				var newURL = new URL(url);
				var shortURL = service.createShortURL(newURL,urlHours.getSeconds());
				return ResponseCreator.getWillBeCreatedResponse(shortURL.toString());
			}  catch (MalformedURLException| InvalidArgumentException e) {
				return ResponseCreator.getBadRequestResponse();
			} catch (ServiceException e) {
				return ResponseCreator.getInternalErrorResponse();
			}
		} else {
			return ResponseCreator.getBadRequestResponse();
		}
	}

}
