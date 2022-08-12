package createshorturl.apigateway;

import static shorturls.apigateway.ResponseCreator.getBadRequestResponse;
import static shorturls.apigateway.ResponseCreator.getInternalErrorResponse;
import static shorturls.apigateway.ResponseCreator.getWillBeCreatedResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

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

	public CreateShortURL(Service service) {
		this.service = service;
	}

	private APIGatewayProxyResponseEvent handleURLHours(final String body, final APIGatewayProxyRequestEvent input) {
		try{
			System.out.println("body "+body);
			val urlHours=mapper.readValue(body,URLHours.class);
			val url = urlHours.getUrl();
			System.out.println("url "+url);
			val hours=urlHours.getHours();
			System.out.println("hours "+hours);
			if (hours <=0){
				System.out.println("hours not valid");
				return ResponseCreator.getBadRequestResponse();
			}
				
			val isValid = UrlValidator.getInstance().isValid(url);
			if (isValid) {
				try {
					System.out.println("url isValid "+isValid);
					val newURL = new URL(url);
					val  shortURL = service.createShortURL(newURL,urlHours.getHours());
					return ResponseCreator.getWillBeCreatedResponse(shortURL.toString());
				}  catch (MalformedURLException| InvalidArgumentException e) {
					System.out.println("mal formed url ");
					return ResponseCreator.getBadRequestResponse();
				} catch (ServiceException e) {
					return ResponseCreator.getInternalErrorResponse();
				}
			} else {
				System.out.println("url isn't valid");
				return ResponseCreator.getBadRequestResponse();
			}
		}catch(JsonProcessingException e){
			System.out.println("parse json exception");
			return ResponseCreator.getBadRequestResponse();
		}
	}

	private APIGatewayProxyResponseEvent handleURL(final String body, final APIGatewayProxyRequestEvent input) {
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
		val contentType = input.getHeaders().get("content-type");
		System.out.println("content-type "+contentType);
		val body = input.getBody();
		System.out.println("body "+body);
		if ("text/plain".equals(contentType)){
			return handleURL(body,input);
		}else if (("application/json").equals(contentType)){
			return handleURLHours(body,input);
		}else{
			return getBadRequestResponse();
		}
	}

}
