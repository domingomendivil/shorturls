package shorturls.apigateway;


import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class ResponseCreator {
	
	private ResponseCreator() {
		//this class cannot be instantiated
	}


	
	private static  APIGatewayProxyResponseEvent getTextResponse() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "text/html");
		return new APIGatewayProxyResponseEvent().withHeaders(headers);
	}
	
	public static  APIGatewayProxyResponseEvent getNotFoundResponse() {
		 var response  = getTextResponse();
		 return response.withStatusCode(404).withBody("URL not found");
	}
	

	public static  APIGatewayProxyResponseEvent getBadRequestResponse() {
		 var response  = getTextResponse();
		 return response.withStatusCode(400).withBody("Invalid URL");
	}

	public static APIGatewayProxyResponseEvent getMovedResponse(String url) {
		 Map<String,String> headers = new HashMap<>();
		 headers.put("Location",url);
		 return new APIGatewayProxyResponseEvent().withStatusCode(301).withHeaders(headers);
	}
	
	
	public static APIGatewayProxyResponseEvent getOKResponse(String body) {
		 var response  = getTextResponse();
		 return response.withStatusCode(200).withBody(body);
		
	}
	public static APIGatewayProxyResponseEvent getInternalErrorResponse() {
		 var response  = getTextResponse();
		 return response.withStatusCode(500).withBody("An Internal Error has ocurred");
	}

	public static APIGatewayProxyResponseEvent getWillBeCreatedResponse(String url) {
		var response  = getTextResponse();
		return response.withStatusCode(201).withBody(url);
   }
	
	public static APIGatewayProxyResponseEvent getURLCreatedResponse(String url) {
		 var response  = getTextResponse();
		 return response.withStatusCode(202).withBody(url);
		
	}
	
	
	
	
	
}
