package shorturls.events.dynamo;

import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromN;
import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromS;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.meli.events.Events;

import lombok.val;
import shorturls.random.Randomizer;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

/**
 * This class is just for simulation purposes. To be scalable, 
 * events should be directly written to a scalable stream queue like
 * Kafka or Kinesis, and after that, events must be obtained from the
 * queue and written to DynamoDB. This avoids high throughput problems
 * to DynamoDB. 
 * 
 */
public class DynamoEvents implements Events{

	/*
	 * Client for connecting to DynamoDb
	 */
	private final DynamoDbClient client;
	

	/*
	 * Class utility for generating random integers
	 */
	private final Randomizer randomizer;

	
	/*
	 * The table used for storing statistic for each short URL
	 */
	private static final String TABLE_URL_STATISTICS ="URLStats";

	/*
	 * The key used for the short URL (short code after the base URL).
	 */
	private static final String PK="shortPath";

	/*
	 * The sort key used for the short URL
	 */
	private static final String METADATA="metadata";
	
	/*
	 * Attribute value for incrementing counters in 1
	 */
	private static final AttributeValue one = fromN("1");

	

	public DynamoEvents(DynamoDbClient client,Randomizer randomizer) {
		this.client = client; 
		this.randomizer= randomizer;
	}
	
	private UpdateItemRequest.Builder getUpdateRequest(Map<String,AttributeValue> itemKey,String expression) {
		 return UpdateItemRequest.builder()
			.tableName(TABLE_URL_STATISTICS).key(itemKey)
			.returnValues(ReturnValue.UPDATED_NEW)
			.updateExpression(expression);
	}
	

	private void updateItem(String shortPath,String key,String value){
		val itemKey = new HashMap<String, AttributeValue>();
		itemKey.put(PK, fromS(shortPath+randomizer.getRandomInt()));
		itemKey.put(METADATA,fromS(key));
		val attributeValues = new HashMap<String, AttributeValue>();
		attributeValues.put(":incr", one);
		attributeValues.put(":evalue",fromS(value));
		UpdateItemRequest request = getUpdateRequest(itemKey,"set evalue= :evalue, ecounter = ecounter +:incr")
				.conditionExpression("attribute_exists(ecounter)").expressionAttributeValues(attributeValues).build();
		try{
			 client.updateItem(request);
		}catch(ConditionalCheckFailedException e){
			val request2 = getUpdateRequest(itemKey,"set evalue= :evalue, ecounter = :incr").expressionAttributeValues(attributeValues).build();
			client.updateItem(request2);
		}
	}



	
	@Override
	public void send(String shortPath, Map<String,String> msg) {
		CompletableFuture.runAsync( ()-> {
			Iterator<String> it = msg.keySet().iterator();
			while (it.hasNext()) {
				String nextKey = it.next();
				updateItem(shortPath,nextKey,msg.get(nextKey));
			}
		});
		
	}


	
}
