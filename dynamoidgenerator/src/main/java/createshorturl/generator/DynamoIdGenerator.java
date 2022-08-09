package createshorturl.generator;

import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromN;
import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromS;

import java.math.BigInteger;
import java.util.HashMap;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

public class DynamoIdGenerator implements IDGenerator {

	private static final String TABLE_URL_ITEM = "URLItem";

	private final DynamoDbClient client;
	
	private final Base62Encoder encoder;
	
	private static final String PK="shortURL";
	
	DynamoIdGenerator(DynamoDbClient client,Base62Encoder encoder) {
		this.client=client;
		this.encoder=encoder;
	}
	
	@Override
	public String generateUniqueID() {
		var nro = nextCounter();
		return encoder.encode(nro);
	}
	
	private BigInteger nextCounter() {
		HashMap<String, AttributeValue> itemKey = new HashMap<>();
		itemKey.put(PK, fromS("counter"));
		HashMap<String, AttributeValue> attributeValues = new HashMap<>();
		attributeValues.put(":incr", fromN("1"));
		UpdateItemRequest request = UpdateItemRequest.builder()
				.tableName(TABLE_URL_ITEM).key(itemKey)
				.returnValues(ReturnValue.UPDATED_NEW)
				.updateExpression("SET LastID = LastID + :incr")
				.expressionAttributeValues(attributeValues).build();
		var response  = client.updateItem(request);
		var nro =response.attributes().get("LastID");
		return new BigInteger(nro.n(),10);
	}



}
