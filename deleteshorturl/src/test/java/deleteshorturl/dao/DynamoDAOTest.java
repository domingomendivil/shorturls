package deleteshorturl.dao;

import java.net.URI;

import shorturls.dynamodao.DynamoDAO;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

//@RunWith(MockitoJUnitRunner.class)
public class DynamoDAOTest {

    private static final  String TABLE_URL_ITEM = "URLItem";

	
	private static final String PK="shortURL";

  //  @InjectMocks
    private DynamoDAO dynamoDAO;

    //@Mock
    private DynamoDbClient client;

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


    private static synchronized DynamoDbClient getDynamoDBClient(String dynamoURL) {
        if ((dynamoURL==null) || (dynamoURL.equals(""))){
            return  DynamoDbClient.create();
        }else {
            return DynamoDbClient
            .builder().
            endpointOverride(URI.create(dynamoURL)).build();
        }
    }

    public static void main(String[] args) {
        DynamoDbClient dbClient = getDynamoDBClient("http://localhost:8000");
        new DynamoDAO(dbClient).deleteById("00000");
    }
    
}
