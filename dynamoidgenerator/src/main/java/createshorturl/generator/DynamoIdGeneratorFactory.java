package createshorturl.generator;

import java.net.URI;

import lombok.Getter;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Factory for creating an instance of the DynamoIdGenerator
 */
public class DynamoIdGeneratorFactory {
	
	@Getter(lazy=true) //Lombok Getter annotation is used to facilitate getting an instance lazily
	private static final DynamoIdGenerator instance = init();
	
	
	/*
	 * System property to know the URL to connect to Dynamodb 
	 */
	private static final String dynamoURL = System.getenv("DYNAMO_URL");
	
	/*
	 * Method for creating a new instance of the DynamoIdGenerator class
	 */
	public final static DynamoIdGenerator init() {
		DynamoDbClient client;
        if ((dynamoURL==null) || (dynamoURL.equals(""))){
            client=  DynamoDbClient.create();  //If no URL is provided, it's implicitly obtained from AWS 
        }else {  
            client = DynamoDbClient
            .builder().
            endpointOverride(URI.create(dynamoURL)).build();
        }
		Encoder base62Encoder = new Base62EncoderImpl(); //a base62 encoder is used by default
		return new DynamoIdGenerator(client, base62Encoder);
	}
	

}
