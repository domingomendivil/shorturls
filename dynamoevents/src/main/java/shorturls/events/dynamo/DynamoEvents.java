package shorturls.events.dynamo;

import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromN;
import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromS;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import lombok.val;
import shorturls.exceptions.ShortURLRuntimeException;
import shorturls.random.Randomizer;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import com.meli.events.Events;

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
	private final DynamoDbAsyncClient client;
	

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

	

	public DynamoEvents(DynamoDbAsyncClient client,Randomizer randomizer) {
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
		System.out.println("shortpath "+shortPath+" key: "+key +"  value: "+value);
		val itemKey = new HashMap<String, AttributeValue>();
		itemKey.put(PK, fromS(shortPath+randomizer.getRandomInt()));
		itemKey.put(METADATA,fromS(key));
		val attributeValues = new HashMap<String, AttributeValue>();
		attributeValues.put(":incr", one);
		attributeValues.put(":evalue",fromS(value));
		UpdateItemRequest request = getUpdateRequest(itemKey,"set evalue= :evalue, ecounter = ecounter +:incr")
				.conditionExpression("attribute_exists(ecounter)").expressionAttributeValues(attributeValues).build();
		try{
			 client.updateItem(request).get();
		}catch(ExecutionException e){
			val request2 = getUpdateRequest(itemKey,"set evalue= :evalue, ecounter = :incr").expressionAttributeValues(attributeValues).build();
			client.updateItem(request2);
		}catch (InterruptedException e) {
			e.printStackTrace();
			throw new ShortURLRuntimeException("");
		}
	}



	
	@Override
	public void send(String shortPath, Map<String,String> msg) {
		CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
			Iterator<String> it = msg.keySet().iterator();
			while (it.hasNext()) {
				String nextKey = it.next();
				updateItem(shortPath,nextKey,msg.get(nextKey));
			}
		});
	}


	
}
