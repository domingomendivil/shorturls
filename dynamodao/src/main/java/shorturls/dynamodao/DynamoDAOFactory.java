package shorturls.dynamodao;

import java.net.URI;

import lombok.Getter;
import shorturls.config.ShortURLProperties;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoDAOFactory {

 
    private DynamoDAOFactory(){

    }

    @Getter(lazy=true) private static final DynamoDAO instance = init();

    private static DynamoDAO init(){
		 String dynamoURL = new ShortURLProperties().getProperty("DYNAMO_URL");
    	 var client = getDynamoDBClient(dynamoURL);
       return new DynamoDAO(client);
    }


	private static final synchronized DynamoDbAsyncClient getDynamoDBClient(String dynamoURL) {
		if ((dynamoURL == null) || (dynamoURL.equals(""))) {
			return DynamoDbAsyncClient.create();
		} else {
			return DynamoDbAsyncClient.builder().endpointOverride(URI.create(dynamoURL)).build();
		}
	}

    
}
