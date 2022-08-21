package shorturls.events.dynamo;

import java.util.Map;

import org.mockito.Mock;

import shorturls.random.Randomizer;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

//@RunWith(MockitoJUnitRunner.class)
public class DynamoEventsTest {
	
	//@InjectMocks
	private DynamoEvents dynamoEvents;
	
	//@Mock
	private Randomizer randomizer;
	

	//@Mock
	private DynamoDbClient client;

	/**private UpdateItemRequest.Builder getUpdateRequest(Map<String,AttributeValue> itemKey,String expression) {
		return UpdateItemRequest.builder()
		   .tableName(TABLE_URL_STATISTICS).key(itemKey)
		   .returnValues(ReturnValue.UPDATED_NEW)
		   .updateExpression(expression);
   }
	
	/**@Test
	public void test1() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("location","Argentina");
		when(randomizer.getRandomInt()).thenReturn(1);
		dynamoEvents.send("code", map);
		val request = getUpdateRequest(itemKey, expression)
		Mockito.verify(client).updateItem(request);
	}
    */
}
