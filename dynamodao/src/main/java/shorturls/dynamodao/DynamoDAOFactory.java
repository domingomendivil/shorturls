package shorturls.dynamodao;

import java.net.URI;

import com.meli.dynamo.DynamoDbClientFactory;

import lombok.Getter;
import lombok.val;
import shorturls.config.ShortURLProperties;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
/**
 * Factory class for instantiating DynamoDAO
 * 
 *
 */
public class DynamoDAOFactory {

 
    private DynamoDAOFactory(){
    	//Factory class cannot be instantiated
    }

    /*
     * Lombok Getter annotation for lazily instantiating the DynamoDAO class 
     */
    @Getter(lazy=true) private static final DynamoDAO instance = init();

    /*
     * Creates a new DynamoDAO class, injecting the DynamoDbAsyncClient instance
     */
    private static final DynamoDAO init(){
       val client = DynamoDbClientFactory.getClient();
       return new DynamoDAO(client);
    }


    
}
