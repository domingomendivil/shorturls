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

    
    /**@Test
    public void test1(){
        String shortPath="As";
        HashMap<String, AttributeValue> itemKey = new HashMap<>();
		itemKey.put(PK, AttributeValue.fromS(shortPath));
        var request= DeleteItemRequest.builder().tableName(TABLE_URL_ITEM)
        .key(itemKey)
        .returnValues(ReturnValue.ALL_OLD)
        .build(); 
        
        Map<String, AttributeValue> attributes = new HashMap<>();
        DeleteItemResponse res= DeleteItemResponse
        .builder()
        .attributes(attributes)
        .build();
        when(client.deleteItem(request)).thenReturn(res);
        assertEquals(false,dynamoDAO.deleteById(shortPath));
    }**/

   /** @Test
    public void test2(){
        String shortPath="As";
        HashMap<String, AttributeValue> itemKey = new HashMap<>();
		itemKey.put(PK, AttributeValue.fromS(shortPath));
        var request= DeleteItemRequest.builder().tableName(TABLE_URL_ITEM)
        .key(itemKey)
        .returnValues(ReturnValue.ALL_OLD)
        .build(); 
        
        Map<String, AttributeValue> attributes = new HashMap<>();
        attributes.put(PK, fromS(shortPath));
        SdkHttpResponse resp = SdkHttpResponse.builder().statusCode(200).build();
        SdkResponse sdkResponse= DeleteItemResponse
        .builder()
        .sdkHttpResponse(resp)
        .build();


        DeleteItemResponse res = DeleteItemResponse.builder()
        when(client.deleteItem(request)).thenReturn(res);
        assertEquals(true,dynamoDAO.deleteById(shortPath));
    } */


}
