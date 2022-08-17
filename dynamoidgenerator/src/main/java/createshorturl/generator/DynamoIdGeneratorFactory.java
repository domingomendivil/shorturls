package createshorturl.generator;

import com.meli.dynamo.DynamoDbClientFactory;

import lombok.Getter;
import lombok.val;
import shorturls.config.ConfigurationException;
import shorturls.config.ShortURLProperties;

/**
 * Factory for creating an instance of the DynamoIdGenerator
 */
public class DynamoIdGeneratorFactory {
	
	@Getter(lazy=true) //Lombok Getter annotation is used to facilitate getting an instance lazily
	private static final DynamoIdGenerator instance = init(new ShortURLProperties());
	
	
	
	/*
	 * Method for creating a new instance of the DynamoIdGenerator class
	 */
	public final static DynamoIdGenerator init(ShortURLProperties properties) {
		val client = DynamoDbClientFactory.getClient();
		Encoder base62Encoder = new Base62EncoderImpl(); //a base62 encoder is used by default
		 val str = properties.getProperty("DYNAMO_RANDOM_RANGE");
		try {
	            Long randomRange = Long.parseLong(str);
	            Randomizer randomizer = new Randomizer(randomRange);
	            return new DynamoIdGenerator(client, base62Encoder,randomizer);
	            
	        }catch (NumberFormatException e){
	            throw new ConfigurationException("Error parsing DYNAMO_RANDOM_RANGE number",e);
	        }
	}
	

}
