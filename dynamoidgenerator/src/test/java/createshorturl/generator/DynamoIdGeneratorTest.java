package createshorturl.generator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromN;
import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromS;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import lombok.val;
import shorturls.exceptions.ShortURLRuntimeException;
import shorturls.random.Randomizer;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

/**
 * Test class for DynamoIdGenerator
 */
@RunWith(MockitoJUnitRunner.class)
public class DynamoIdGeneratorTest {
	
	@Mock
	private Encoder encoder;
	
	@Mock
	private DynamoDbAsyncClient client = Mockito.mock(DynamoDbAsyncClient.class);
	
	@Mock
	private Randomizer randomizer;
	
	@InjectMocks
	private DynamoIdGenerator idGenerator;
	
	@Test
	public void test1() {
		HashMap<String, AttributeValue> itemKey = new HashMap<>();
		when(randomizer.getRandomInt()).thenReturn(5);
		itemKey.put("shortURL", fromS("counter5"));
		val attributeValues = new HashMap<String, AttributeValue>();
		attributeValues.put(":incr", fromN("1"));
		UpdateItemRequest request = UpdateItemRequest.builder()
				.tableName("URLItem").key(itemKey)
				.returnValues(ReturnValue.UPDATED_NEW)
				.updateExpression("SET LastID = LastID + :incr")
				.expressionAttributeValues(attributeValues).build();
		AttributeValue attr = AttributeValue.fromN("10");
		val values = new HashMap<String, AttributeValue>();
		values.put("LastID", attr);
		UpdateItemResponse response = UpdateItemResponse.builder().attributes(values).build();
		CompletableFuture<UpdateItemResponse> res = CompletableFuture.completedFuture(response);
		when(client.updateItem(request)).thenReturn(res);
		when(encoder.encode(BigInteger.valueOf(10L))).thenReturn("ASD");
		assertEquals("ASD",idGenerator.generateUniqueID());
	}
	

}
