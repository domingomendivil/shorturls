package shorturls.apigateway;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringJoiner;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import lombok.val;
import shorturls.exceptions.ShortURLRuntimeException;

/**
 * Utility class for generating HTTP responses with different status codes
 * and headers and bodies
 */
public class ResponseCreator {

	private ResponseCreator() {
		// this class cannot be instantiated
	}

	/*
	 * Method that randomically generates a string used for session ids that are used
	 * for HTTP responses in cookies that are set in the header
	 */
	private static final String random() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		Random random = new Random();

		return random.ints(leftLimit, rightLimit + 1).filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
	}

	private static APIGatewayProxyResponseEvent getTextResponse() {
		val headers = new HashMap<String, String>();
		headers.put("content-type", "text/plain; charset=utf-8");
		return new APIGatewayProxyResponseEvent().withHeaders(headers);
	}

	public static APIGatewayProxyResponseEvent getNotFoundResponse() {
		val response = getTextResponse();
		return response.withStatusCode(404).withBody("URL not found");
	}

	public static APIGatewayProxyResponseEvent getBadRequestResponse() {
		var response = getTextResponse();
		return response.withStatusCode(400).withBody("Invalid URL");
	}

	public static APIGatewayProxyResponseEvent getMovedResponse(Map<String, String> reqHeaders, String url,String cookieConfig) {

		val headers = new HashMap<String, String>();
		try {
			headers.put("Location", toPuniCode(url));
			headers.put("content-type", "text/plain; charset=utf-8");
			if (reqHeaders != null) {
				val cookie = reqHeaders.get("Cookie");
				if (cookie == null) {
					String sessionId = random();
					String newCookie = "sessionid=" + sessionId + cookieConfig;
					headers.put("Set-Cookie", newCookie);
				}
			}

			return new APIGatewayProxyResponseEvent().withStatusCode(301).withHeaders(headers);
		}catch (MalformedURLException e) {
			throw new ShortURLRuntimeException("Error encoding URL "+url,e);
		}
	}

	public static APIGatewayProxyResponseEvent getOKResponse(String body) {
		val response = getTextResponse();
		return response.withStatusCode(200).withBody(body);
	}

	public static APIGatewayProxyResponseEvent getOKResponseCookie(String body) {
		val response = getTextResponse();
		return response.withStatusCode(200).withBody(body);
	}

	public static APIGatewayProxyResponseEvent getInternalErrorResponse() {
		val response = getTextResponse();
		return response.withStatusCode(500).withBody("An Internal Error has ocurred");
	}

	public static APIGatewayProxyResponseEvent getWillBeCreatedResponse(String url) {
		val response = getTextResponse();
		return response.withStatusCode(201).withBody(url);
	}

	public static APIGatewayProxyResponseEvent getURLCreatedResponse(String url) {
		val response = getTextResponse();
		return response.withStatusCode(202).withBody(url);

	}
	
	private static String toPuniCode(String url) throws MalformedURLException {
		URL u = new URL(url);
		String[] labels = u.getHost().split("\\.");
		StringJoiner joiner = new StringJoiner(".");
		for (int i=0;i<labels.length;i++) {
			String str= java.net.IDN.toASCII(labels[i]);
			joiner.add(str);
		}
		String punicoded =joiner.toString();
		punicoded=u.getProtocol()+"://"+punicoded;
		return punicoded;
	}

}
