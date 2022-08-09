package shorturl.idgenerator;

import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromN;
import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromS;

import java.math.BigInteger;
import java.util.HashMap;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

public class DynamoIDGenerator implements IDGenerator {

	private final static String TABLE_URL_ITEM = "URLItem";

	private DynamoDbClient client;
	
	private Base62Encoder encoder;

	
	private final static String PK="shortURL";
	
	public DynamoIDGenerator(DynamoDbClient client,Base62Encoder encoder) {
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
		System.out.println("nro "+nro.n());
		return new BigInteger(nro.n(),10);
	}
	


}
