package deleteshorturl.apigateway;

import static shorturls.apigateway.ResponseCreator.getBadRequestResponse;
import static shorturls.apigateway.ResponseCreator.getNotFoundResponse;
import static shorturls.apigateway.ResponseCreator.getOKResponse;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import deleteshorturl.services.Service;
import lombok.val;
import shorturls.exceptions.InvalidArgumentException;

/**
 * Class for handling HTTP requests for deleting short URLs.
 * 
 */
public class DeleteShortPath {

	/*
	 * The Service Layer to actually make de the deletion of the URL
	 */
	private final Service service;

	public DeleteShortPath(Service service) {
		this.service = service;
	}

	/*
	 * Method Handler. It parses the HTTP request, validates it and
	 * delegates the deletion to the Service Layer.
	 */
	public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input) {
		val pars = input.getPathParameters();
		if (pars != null) {
			String shortPath = pars.get("code");
			if (shortPath != null)
				try {
					if (service.deleteURL(shortPath))
						return getOKResponse("URL deleted");
					else
						return getNotFoundResponse();
				} catch (InvalidArgumentException e) {
					// bad request, return next in code
				}
		}
		return getBadRequestResponse();
	}

}
