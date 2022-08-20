package shorturls.events.dynamo.config;

import com.meli.dynamo.DynamoDbClientFactory;

import lombok.Getter;
import lombok.val;
import shorturls.config.ConfigurationException;
import shorturls.config.ShortURLProperties;
import shorturls.config.ShortURLPropertiesImpl;
import shorturls.events.dynamo.DynamoEvents;
import shorturls.random.Randomizer;
import shorturls.random.RandomizerImpl;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;



public class DynamoEventsFactory {

  
    @Getter(lazy=true) //Lombok Getter annotation is used to facilitate getting an instance lazily
	private static final DynamoEvents instance = init(new ShortURLPropertiesImpl());

    private static final DynamoEvents init(ShortURLProperties properties){
    	DynamoDbClient client = DynamoDbClientFactory.getClient();
        val str = properties.getProperty("DYNAMO_RANDOM_RANGE");
        try {
            Integer randomRange = Integer.parseInt(str);
            Randomizer randomizer = new RandomizerImpl(randomRange);
            return new DynamoEvents(client,randomizer);
            
        }catch (NumberFormatException e){
            throw new ConfigurationException("Error parsing DYNAMO_RANDOM_RANGE number",e);
        }
        
    }

}
