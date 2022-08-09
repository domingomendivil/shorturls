package createshorturl.generator;

import java.net.URI;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoIdGeneratorFactory {
	
	private static DynamoIdGenerator dynamoIdGenerator;
	
	private static final String dynamoURL = System.getenv("DYNAMO_URL");
	
	public final static synchronized DynamoIdGenerator getDynamoIdGenerator() {
		if (dynamoIdGenerator ==null) {
			DynamoDbClient client;
	        if ((dynamoURL==null) || (dynamoURL.equals(""))){
	            client=  DynamoDbClient.create();
	        }else {
	            client = DynamoDbClient
	            .builder().
	            endpointOverride(URI.create(dynamoURL)).build();
	        }
			Base62Encoder base62Encoder = new Base62EncoderImpl();
			dynamoIdGenerator = new DynamoIdGenerator(client, base62Encoder);
		}
		return dynamoIdGenerator;
	}
	

}
