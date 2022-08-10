package shorturls.dynamodao;

import static org.mockito.Mockito.when;
import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromS;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import lombok.val;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;

public class DynamoDAOTest {

    @InjectMocks
    private DynamoDAO dynamoDAO;

    @Mock
    private DynamoDbClient client;

    public void test1(){

    }
    @Test
    /**public void testDeleteById1(){
        val key = new HashMap<String, AttributeValue> ();
        key.put("shortPath", fromS("a"));
        val req = DeleteItemRequest.builder()
        .key(key).build();
        DeleteItemResponse response = DeleteItemResponse.builder()
        .build();
        when(client.deleteItem(req)).thenReturn(response);
        dynamoDAO.deleteById("a");
    }**/

    

}
