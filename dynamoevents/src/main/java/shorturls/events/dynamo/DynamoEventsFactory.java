package shorturls.events.dynamo;

import java.net.URI;

import lombok.Getter;
import shorturls.config.ConfigurationException;
import shorturls.config.ShortURLProperties;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;



public class DynamoEventsFactory {

  
    @Getter(lazy=true) //Lombok Getter annotation is used to facilitate getting an instance lazily
	private static final DynamoEvents instance = init();

    private static final DynamoEvents init(){
        ShortURLProperties properties = new ShortURLProperties();
    
        String dynamoURL = properties.getProperty("DYNAMO_URL");
    
        DynamoDbAsyncClient client;
        if ((dynamoURL==null) || (dynamoURL.equals(""))){
            client=  DynamoDbAsyncClient.create();  //If no URL is provided, it's implicitly obtained from AWS 
        }else {  
            client = DynamoDbAsyncClient
            .builder().
            endpointOverride(URI.create(dynamoURL)).build();
        }
        String str = properties.getProperty("DYNAMO_RANDOM_RANGE");
        try {
            Long randomRange = Long.parseLong(str);
            return new DynamoEvents(client,randomRange);
            
        }catch (NumberFormatException e){
            throw new ConfigurationException("Error parsing DYNAMO_RANDOM_RANGE number",e);
        }
        
    }

}
