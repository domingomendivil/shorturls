package com.meli.dynamo;

import java.net.URI;

import lombok.Getter;
import lombok.val;
import shorturls.config.ShortURLProperties;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

public class DynamoDbClientFactory {

	private DynamoDbClientFactory() {
		
	}
	
	@Getter(lazy=true)
	private static final DynamoDbAsyncClient client = getClient(new ShortURLProperties());
	
	private static final DynamoDbAsyncClient getClient(ShortURLProperties props) {
		val dynamoURL = props.getProperty("DYNAMO_URL");
        DynamoDbAsyncClient client;
        if ((dynamoURL==null) || (dynamoURL.equals(""))){
            client=  DynamoDbAsyncClient.create();  //If no URL is provided, it's implicitly obtained from AWS 
        }else {  
            client = DynamoDbAsyncClient
            .builder().
            endpointOverride(URI.create(dynamoURL)).build();
        }
        return client;
	}   
	
}
