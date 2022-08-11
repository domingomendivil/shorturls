package shorturls.dynamodao;

import java.net.URI;

import lombok.Getter;
import shorturls.config.ShortURLProperties;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoDAOFactory {

    private static final String dynamoURL = new ShortURLProperties().getProperty("DYNAMO_URL");

    private DynamoDAOFactory(){

    }

    @Getter(lazy=true) private static final DynamoDAO instance = init();

    private static DynamoDAO init(){
    	 var client = getDynamoDBClient(dynamoURL);
       return new DynamoDAO(client);
    }


	private static final synchronized DynamoDbClient getDynamoDBClient(String dynamoURL) {
		if ((dynamoURL == null) || (dynamoURL.equals(""))) {
			System.out.println("dynamo client ***************");
			return DynamoDbClient.create();
		} else {
			System.out.println("dynamoURL :"+dynamoURL);
			return DynamoDbClient.builder().endpointOverride(URI.create(dynamoURL)).build();
		}
	}

    
}
