package shorturls.dynamodao;

import java.net.URI;

import lombok.Getter;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoDAOFactory {

    private static final String dynamoURL = System.getenv("DYNAMO_URL");

    private DynamoDAOFactory(){

    }

    @Getter(lazy=true) private static final DynamoDAO instance = init();

    private static DynamoDAO init(){
    	 var client = getDynamoDBClient(dynamoURL);
       return new DynamoDAO(client);
    }


	private static final synchronized DynamoDbClient getDynamoDBClient(String dynamoURL) {
		if ((dynamoURL == null) || (dynamoURL.equals(""))) {
			return DynamoDbClient.create();
		} else {
			return DynamoDbClient.builder().endpointOverride(URI.create(dynamoURL)).build();
		}
	}

    
}
