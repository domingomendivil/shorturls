package shorturls.events.dynamo;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import shorturls.random.Randomizer;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

//@RunWith(MockitoJUnitRunner.class)
public class DynamoEventsTest {
	
	//@InjectMocks
	private DynamoEvents dynamoEvents;
	
	//@Mock
	private Randomizer randomizer;
	

	@Mock
	private DynamoDbAsyncClient client;
	
/**	@Test
	public void test1() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("location","Argentina");
		when(randomizer.getRandomInt()).thenReturn(1);
		dynamoEvents.send("code", map);
	}**/
    
}
