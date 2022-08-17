package shorturls.events.dynamo;

import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromN;
import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromS;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.meli.events.Events;

import lombok.val;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
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
	 * DynamoDB client used for interacting and making operations, in
	 * this case updating records
	 */
	private final DynamoDbAsyncClient client;
	
	/*
	 * range used for getting the random suffix in primary keys, 
	 * to avoid hot partitions.
	 */
	private final Long randomRange;

	
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

	

	public DynamoEvents(DynamoDbAsyncClient client,Long randomRange) {
		this.client = client; 
		this.randomRange = randomRange;
	}
	
	
	

	private void updateItem(String shortPath,String key,String value){
		val itemKey = new HashMap<String, AttributeValue>();
		itemKey.put(PK, fromS(shortPath+getRandomSuffix()));
		itemKey.put(METADATA,fromS(key));
		val attributeValues = new HashMap<String, AttributeValue>();
		attributeValues.put(":incr", one);
		attributeValues.put(":evalue",fromS(value));
		val request = UpdateItemRequest.builder()
				.tableName(TABLE_URL_STATISTICS).key(itemKey)
				.returnValues(ReturnValue.UPDATED_NEW)
				.updateExpression("set evalue= :evalue, ecounter = ecounter +:incr")
				.conditionExpression("attribute_exists(ecounter)").expressionAttributeValues(attributeValues).build();
		try{
			 client.updateItem(request);
		}catch(ConditionalCheckFailedException e){
			val request2 = UpdateItemRequest.builder()
			.tableName(TABLE_URL_STATISTICS).key(itemKey)
			.returnValues(ReturnValue.UPDATED_NEW)
			.updateExpression("set evalue= :evalue, ecounter = :incr").expressionAttributeValues(attributeValues).build();
			client.updateItem(request2);
		}
	}



	
	@SuppressWarnings("unchecked")
	@Override
	public void send(String shortPath, Map<String,String> msg) {
		Iterator<String> it = msg.keySet().iterator();
		while (it.hasNext()) {
			String nextKey = it.next();
			updateItem(shortPath,nextKey,msg.get(nextKey));
		}
	}

	/*
	 * This suffix is added to the primary key to avoid "Hot partitions" 
	 * Counters for each of the suffixes must be added together to get the total count
	 * Randomization assures that tha primary keys are uniformly distributed across
	 * all the available DynamoDB partitions.
	 */
	private String getRandomSuffix() {
		double l =Math.floor(Math.random() * (randomRange + 1));
		return l+"";
	}
	
}
