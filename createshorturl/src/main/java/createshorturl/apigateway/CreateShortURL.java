package createshorturl.apigateway;

import static shorturls.apigateway.ResponseCreator.getBadRequestResponse;
import static shorturls.apigateway.ResponseCreator.getInternalErrorResponse;
import static shorturls.apigateway.ResponseCreator.getWillBeCreatedResponse;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.validator.routines.UrlValidator;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import createshorturl.services.Service;
import createshorturl.services.ServiceException;
import lombok.val;
import shorturls.apigateway.ResponseCreator;
import shorturls.exceptions.InvalidArgumentException;


public class CreateShortURL {

	private final Service service;

	private final ObjectMapper mapper = new ObjectMapper();

	static final String CONTENT_TYPE="content-type";

	public CreateShortURL(Service service) {
		this.service = service;
	}
	
	private APIGatewayProxyResponseEvent getShortURL(String url,URLExpire urlExpire){
		try {
			val newURL = new URL(url);
			val  shortURL = service.createShortURL(newURL,urlExpire.getSeconds());
			return ResponseCreator.getWillBeCreatedResponse(shortURL.toString());
		}  catch (MalformedURLException| InvalidArgumentException e) {
			return ResponseCreator.getBadRequestResponse();
		} catch (ServiceException e) {
			return ResponseCreator.getInternalErrorResponse();
		}
	}

	private APIGatewayProxyResponseEvent handleURLExpires(final String body) {
		try{
			val urlExpire=mapper.readValue(body,URLExpire.class);
			val url = urlExpire.getUrl();
			val seconds=urlExpire.getSeconds();
			if (seconds <=0){
				return ResponseCreator.getBadRequestResponse();
			}
			val isValid = UrlValidator.getInstance().isValid(url);
			if (isValid) {
				return getShortURL(url,urlExpire);
			} else {
				return ResponseCreator.getBadRequestResponse();
			}
		}catch(JsonProcessingException e){
			return ResponseCreator.getBadRequestResponse();
		}
	}

	private APIGatewayProxyResponseEvent handleURL(final String body) {
		val isValid = UrlValidator.getInstance().isValid(body);
		if (isValid) {
			try {
				val newURL = new URL(body);
				val shortURL = service.createShortURL(newURL);
				return getWillBeCreatedResponse(shortURL.toString());
			}  catch (MalformedURLException| InvalidArgumentException e) {
				return getBadRequestResponse();
			} catch (ServiceException e) {
				return getInternalErrorResponse();
			}
		} else {
			return getBadRequestResponse();
		}
	}

	
	public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input) {
		val contentType = input.getHeaders().get(CONTENT_TYPE);
		System.out.println(CONTENT_TYPE+": "+contentType);
		val body = input.getBody();
		if ("text/plain".equals(contentType)){
			return handleURL(body);
		}else if (("application/json").equals(contentType)){
			return handleURLExpires(body);
		}else{
			return getBadRequestResponse();
		}
	}

}
