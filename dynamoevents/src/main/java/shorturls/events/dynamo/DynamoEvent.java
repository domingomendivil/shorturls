package shorturls.events.dynamo;

import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromN;
import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromS;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.events.Events;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
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
public class DynamoEvent implements Events{
	
	/*
	 * DynamoDB client used for interacting and making operations, in
	 * this case updating records
	 */
	private final DynamoDbClient client;
	
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
	 * The key used for the short URL (shor code after the base URL).
	 */
	private static final String PK="shortURL";
	
	/*
	 * Attribute value for incrementing counters in 1
	 */
	private static final AttributeValue one = fromN("1");
	
	/*
	 * Object used for converting json to map values and viceversa
	 */
	private final ObjectMapper mapper = new ObjectMapper();
	

	public DynamoEvent(DynamoDbClient client,Long randomRange) {
		this.client = client; 
		this.randomRange = randomRange;
	}
	
	/*
	 * Gets the update expression considering all of the (key,value) pairs
	 * of the map. 
	 */
	private String getUpdateExpression(Map<String,String> map) {
		Iterator<String> it = map.keySet().iterator();
		StringBuilder sb = new StringBuilder();
		sb.append("SET ");
		while (it.hasNext()) {
			String key =it.next();
			sb.append(key+"-"+map.get(key));
			sb.append(" = ");
			sb.append(it.next());
			sb.append(" + :incr ,");
		}
		String str= sb.toString();
		int index= str.length();
		return str.substring(0,index-1);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public void send(String key, String msg) {
		
		Map<String, String> map;
		try {
			map = mapper.readValue(msg, Map.class);
			HashMap<String, AttributeValue> itemKey = new HashMap<>();
			itemKey.put(PK, fromS(key+getRandomSuffix()));
			HashMap<String, AttributeValue> attributeValues = new HashMap<>();
			attributeValues.put(":incr", one);
			String updateExpression = getUpdateExpression(map);
			UpdateItemRequest request = UpdateItemRequest.builder()
					.tableName(TABLE_URL_STATISTICS).key(itemKey)
					.returnValues(ReturnValue.UPDATED_NEW)
					.updateExpression(updateExpression)
					.expressionAttributeValues(attributeValues).build();
			client.updateItem(request);
		} catch (JsonProcessingException e) {
			//this error should never happen, as the Json String
			//is generated securely from the HTTP Get headers when 
			//redirecting URLs from short URLs
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
