package createshorturl.generator;

import static org.mockito.Mockito.when;
import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromN;
import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromS;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

/**
 * Test class for DynamoIdGenerator
 */
@RunWith(MockitoJUnitRunner.class)
public class DynamoIdGeneratorTest {
	
	@InjectMocks
	private DynamoIdGenerator idGenerator;
	
	@Mock
	private Encoder encoder;
	
	@Mock
	private DynamoDbClient client;
	
	
	@Test
	public void test1() {
		HashMap<String, AttributeValue> itemKey = new HashMap<>();
		itemKey.put("shortURL", fromS("counter"));
		HashMap<String, AttributeValue> attributeValues = new HashMap<>();
		attributeValues.put(":incr", fromN("1"));
		UpdateItemRequest request = UpdateItemRequest.builder()
				.tableName("URLItem").key(itemKey)
				.returnValues(ReturnValue.UPDATED_NEW)
				.updateExpression("SET LastID = LastID + :incr")
				.expressionAttributeValues(attributeValues).build();
		AttributeValue attr = AttributeValue.fromN("10");
		Map<String, AttributeValue> values = new HashMap<>();
		values.put("LastID", attr);
		UpdateItemResponse response = UpdateItemResponse.builder().attributes(values).build();
		when(client.updateItem(request)).thenReturn(response);
		when(encoder.encode(BigInteger.valueOf(10L))).thenReturn("ASD");
		Assert.assertEquals("ASD",idGenerator.generateUniqueID());
	}

}
