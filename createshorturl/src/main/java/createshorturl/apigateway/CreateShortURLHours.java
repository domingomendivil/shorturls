package createshorturl.apigateway;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.validator.routines.UrlValidator;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import createshorturl.services.InvalidArgumentsException;
import createshorturl.services.Service;
import createshorturl.services.ServiceException;
import shorturls.apigateway.ResponseCreator;

public class CreateShortURLHours {

	private Service service;

	public CreateShortURLHours(Service service) {
		this.service = service;
	}

	public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input) {
		String body = input.getBody();
		ObjectMapper mapper = new ObjectMapper();
		URLHours urlHours;
		try{
			urlHours=mapper.readValue(body,URLHours.class);
		}catch(JsonProcessingException e){
			return ResponseCreator.getBadRequestResponse();
		}
			
		var url = urlHours.getUrl();
		var hours=urlHours.getHours();
		if (hours <=0)
			return ResponseCreator.getBadRequestResponse();
		boolean isValid = UrlValidator.getInstance().isValid(url);
		if (isValid) {
			try {
				var newURL = new URL(url);
				var shortURL = service.createShortURL(newURL,urlHours.getHours());
				return ResponseCreator.getWillBeCreatedResponse(shortURL.toString());
			}  catch (MalformedURLException| InvalidArgumentsException e) {
				return ResponseCreator.getBadRequestResponse();
			} catch (ServiceException e) {
				return ResponseCreator.getInternalErrorResponse();
			}
		} else {
			return ResponseCreator.getBadRequestResponse();
		}
	}

}
