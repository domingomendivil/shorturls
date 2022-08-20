package createshorturl.generator;

import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromN;
import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromS;

import java.math.BigInteger;
import java.util.HashMap;

import lombok.val;
import shorturls.random.Randomizer;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

/**
 * This class implements IDGenerator interface for obtaining a unique short code used in short URLs.
 * A base62 encoded counter is used, which is an incremented atomic counter calculated in DynamoDb.
 * This code, should not be used as secure code with secure short URLs, 
 * because it's not securely generated, as it can be easily guessed.
 * It can be used safely accross one AWS region, to avoid collisions in generating short URLs. 
 * The operation is not idempotent, that means that if connection goes down after you update
 * you won't know if the update was successful, and where you should retry the operation. 
 * Another problem you have with using dynamodb counters is the hot partition problem.
 */
public class DynamoIdGenerator implements IDGenerator {
	
	/*
	 * Attribute value for incrementing one to the counter
	 */
	private static final AttributeValue one = fromN("1");

	/*
	 * Table used for storing the atomic counter
	 */
	
	private static final String TABLE_URL_ITEM = "URLItem";

	/*
	 * DynamoDb client used for connecting and making operations. In this case, update the counter.
	 */
	private final DynamoDbClient client;
	
	/*
	 * Encoder used to encode the counter stored.
	 */
	private final Encoder encoder;
	
	/*
	 * ShortURL is the name of the primary key where the counter is stored 
	 */
	private static final String PK="shortURL";
	
	/*
	 * Randomizer for getting the random suffix in primary keys, 
	 * to avoid hot partitions.
	 */
	private final Randomizer randomizer;
	
	/*
	 * constructor class
	 */
	public DynamoIdGenerator(DynamoDbClient client,Encoder encoder,Randomizer randomizer) {
		this.client=client;
		this.encoder=encoder;
		this.randomizer = randomizer;
	}
	
	
	@Override
	public String generateUniqueID() {
		var nro = nextCounter();
		return encoder.encode(nro);
	}
	
	/*
	 * Method for getting the next counter. It basically increments atomically the record
	 * whose key is "shortURL" and value is "counter". The new counter is then returned.
	 */
	private BigInteger nextCounter() {
		val itemKey = new HashMap<String, AttributeValue>();
		itemKey.put(PK, fromS("counter"+randomizer.getRandomInt()));
		val attributeValues = new HashMap<String, AttributeValue>();
		attributeValues.put(":incr", one);
		UpdateItemRequest request = UpdateItemRequest.builder()
				.tableName(TABLE_URL_ITEM).key(itemKey)
				.returnValues(ReturnValue.UPDATED_NEW)
				.updateExpression("SET LastID = LastID + :incr")
				.expressionAttributeValues(attributeValues).build();

			val response  = client.updateItem(request);
			val nro =response.attributes().get("LastID");
			return new BigInteger(nro.n(),10);
	
	}
	



}
