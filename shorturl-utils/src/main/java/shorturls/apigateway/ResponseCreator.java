package shorturls.apigateway;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class ResponseCreator {
	
	private ResponseCreator() {
		//this class cannot be instantiated
	}


	
private static final String random() {
    int leftLimit = 48; // numeral '0'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 10;
    Random random = new Random();

    return random.ints(leftLimit, rightLimit + 1)
      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
      .limit(targetStringLength)
      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
      .toString();
}
	
	private static  APIGatewayProxyResponseEvent getTextResponse() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "text/plain");
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

	public static APIGatewayProxyResponseEvent getMovedResponse(Map<String,String> reqHeaders,String url) {
		
		 Map<String,String> headers = new HashMap<>();
		 headers.put("Location",url);
		 if (reqHeaders!=null){
			var cookie= reqHeaders.get("Cookie");
			if (cookie==null){
				String sessionId = random();
				String newCookie = "sessionid="+sessionId+"; SameSite=Strict";
				headers.put("Set-Cookie", newCookie);
			 }
		 }
		

		 return new APIGatewayProxyResponseEvent().withStatusCode(301).withHeaders(headers);
	}
	
	
	public static APIGatewayProxyResponseEvent getOKResponse(String body) {
		 var response  = getTextResponse();
		 return response.withStatusCode(200).withBody(body);
	}

	public static APIGatewayProxyResponseEvent getOKResponseCookie(String body) {
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
