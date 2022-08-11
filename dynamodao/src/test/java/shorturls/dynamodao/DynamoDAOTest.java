package shorturls.dynamodao;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoDAOTest {

   // @InjectMocks
    private DynamoDAO dynamoDAO;

    //@Mock
    private DynamoDbClient client;

    public void test1(){

    }
    /**@Test
    public void testDeleteById1(){
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
