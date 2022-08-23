package com.meli.dynamo;

import java.net.URI;

import lombok.Getter;
import lombok.val;
import shorturls.config.ShortURLProperties;
import shorturls.config.ShortURLPropertiesImpl;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Factory class for getting an instance of DynamoDbAsyncClient
 */
public class DynamoDbClientFactory {

	private DynamoDbClientFactory() {
		//no public constructor.
        //class cannot be instantiated
	}
	
	@Getter(lazy=true)
	private static final DynamoDbClient client = getClient(ShortURLPropertiesImpl.getInstance());
	
    /*
     * Creates a new instance of DynamoDbAsynClient
     */
	static final DynamoDbClient getClient(ShortURLProperties props) {
		val dynamoURL = props.getProperty("DYNAMO_URL");
        DynamoDbClient client;
        if ((dynamoURL==null) || (dynamoURL.equals(""))){
            client=  DynamoDbClient.create();  //If no URL is provided, it's implicitly obtained from AWS 
        }else {   //otherwise, the URL provided is used
            client = DynamoDbClient
            .builder().
            endpointOverride(URI.create(dynamoURL)).build();
        }
        return client;
	}   
	
}
