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

/**
 * Class for handling HTTP request for creating short URLs.
 * 
 */
public class CreateShortURL {

	/*
	 * Service layer 
	 */
	private final Service service;

	/*
	 * Jackson mapper to convert JSON input to URLExpire object
	 */
	private final ObjectMapper mapper = new ObjectMapper();

	/*
	 * Constant to compare input content-type and following processing
	 */
	static final String CONTENT_TYPE="content-type";

	/*
	 * Validator of input URLs
	 */
	private UrlValidator urlValidator = UrlValidator.getInstance();

	/**
	 * Constructor with service layer to be injected
	 * @param service The service layer injected
	 */
	public CreateShortURL(Service service) {
		this.service = service;
	}
	
	private APIGatewayProxyResponseEvent getShortURL(String url,URLExpire urlExpire){
		try {
			val newURL = new URL(url);
			val  shortURL = service.createShortURL(newURL,urlExpire.getSeconds());
			return getWillBeCreatedResponse(shortURL.toString());
		}  catch (MalformedURLException| InvalidArgumentException e) {
			return getBadRequestResponse();
		} catch (ServiceException e) {
			return getInternalErrorResponse();
		}
	}

	/*
	 * Handles URLs with expiration time in seconds
	 */
	private APIGatewayProxyResponseEvent handleURLExpires(final String body) {
		try{
			val urlExpire=mapper.readValue(body,URLExpire.class);
			val url = urlExpire.getUrl();
			val seconds=urlExpire.getSeconds();
			if (seconds >0){
				if (urlValidator.isValid(url)) 
					return getShortURL(url,urlExpire);
			}
			return getBadRequestResponse();
		}catch(JsonProcessingException e){
			//in case of error processing the json is because JSON is in wrong format
			//so it is a bad request
			return getBadRequestResponse();
		}
	}

	/*
	 * Handles a simple URL in plain text format
	 */
	private APIGatewayProxyResponseEvent handleURL(final String body) {
		if (urlValidator.isValid(body)) {
			try {
				val newURL = new URL(body);
				val shortURL = service.createShortURL(newURL);
				return getWillBeCreatedResponse(shortURL.toString());
			}  catch (MalformedURLException| InvalidArgumentException e) {
				return getBadRequestResponse();
			} catch (ServiceException e) {
				return getInternalErrorResponse();
			}
		} 
		return getBadRequestResponse();
		
	}

	/*
	 * handler for creating Short URLs. 
	 * Input content can be json or text. In case of json, it receives
	 * in the body a URL in JSON format, indicating the expiration time in seconds
	 * e.g. {"url":"http://www.google.com","seconds":3000}
	 * In case of a text, the body must contain a URL in plain text format
	 */
	public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input) {
		val contentType = input.getHeaders().get(CONTENT_TYPE);
		val body = input.getBody();
		if ("text/plain".equals(contentType)){
			return handleURL(body); //creates a simple URL without expiration time
		}else if (("application/json").equals(contentType)){
			return handleURLExpires(body); //creates a URL with expiration time (json)
		}else{
			return getBadRequestResponse(); //anything else is a bad request
		}
	}

}
