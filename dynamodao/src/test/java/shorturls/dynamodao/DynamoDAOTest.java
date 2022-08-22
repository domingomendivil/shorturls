package shorturls.dynamodao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromN;
import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromS;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import lombok.val;
import shorturls.model.URLItem;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

/**
 * Unit Testing class for DynamoDAO class
 */
@RunWith(MockitoJUnitRunner.class)
public class DynamoDAOTest {

  @InjectMocks
  private DynamoDAO dynamoDAO;

  @Mock
  private DynamoDbClient client;

  private GetItemResponse getResponse(String pk, String longURL, String creationDate, Long expirationTime) {
    Map<String, AttributeValue> item = new HashMap<>();
    item.put(DynamoDAO.LONG_URL, fromS(longURL));
    item.put(DynamoDAO.PK, fromS(pk));
    item.put(DynamoDAO.CREATION_DATE, fromS(creationDate));
    item.put(DynamoDAO.TTL, fromN(expirationTime.toString()));
    return GetItemResponse.builder().item(item).build();
  }

  private GetItemResponse getEmptyResponse() {
    return GetItemResponse.builder().build();
  }

  private GetItemRequest getRequest(String code) {
    val key = new HashMap<String, AttributeValue>();
    key.put(DynamoDAO.PK, fromS(code));
    return GetItemRequest.builder()
        .tableName(DynamoDAO.TABLE_URL_ITEM)
        .key(key)
        .build();
  }

  private URLItem getItem(String code, String url, String date, Long expirationTime) throws MalformedURLException {
    val creationDate = LocalDateTime.parse(date);
    return new URLItem(code, new URL(url), creationDate, expirationTime);
  }

  @Test
  public void testGetById1() throws MalformedURLException {
    val code = "code";
   
    val dateStr = "2022-08-17T14:05:32.247299";
    val url = "http://www.google.com";
    val response = getResponse(code, url, dateStr, 10L);
    
    val request = getRequest(code);
    when(client.getItem(request)).thenReturn(response);

    val urlItem = getItem(code, url, dateStr, 10L);
    val res = dynamoDAO.getById(code);
    assertEquals(urlItem, res.get());
  }

  @Test
  public void testGetById2() throws MalformedURLException {
    val code = "code";
    
    val response = getEmptyResponse();
    
    val request = getRequest(code);
    when(client.getItem(request)).thenReturn(response);

    val res = dynamoDAO.getById(code);
    assertEquals(Optional.empty(), res);
  }

  /**
   * @Test
   *       public void testInsert() {
   *       val dateStr ="2022-08-17T14:05:32.247299";
   *       URLItem item = getItem("code","http://www.google.com",dateStr,10L);
   *       dynamoDAO.insert(item);
   *       }
   * 
   * 
   *       /** @Test
   *       public void testDeleteById1(){
   *       val key = new HashMap<String, AttributeValue> ();
   *       key.put("shortPath", fromS("a"));
   *       val req = DeleteItemRequest.builder()
   *       .key(key).build();
   *       DeleteItemResponse response = DeleteItemResponse.builder()
   *       .build();
   *       when(client.deleteItem(req)).thenReturn(response);
   *       dynamoDAO.deleteById("a");
   *       }
   * 
   * 
   *       /**@Test
   *       public void test1(){
   *       String shortPath="As";
   *       HashMap<String, AttributeValue> itemKey = new HashMap<>();
   *       itemKey.put(PK, AttributeValue.fromS(shortPath));
   *       var request= DeleteItemRequest.builder().tableName(TABLE_URL_ITEM)
   *       .key(itemKey)
   *       .returnValues(ReturnValue.ALL_OLD)
   *       .build();
   * 
   *       Map<String, AttributeValue> attributes = new HashMap<>();
   *       DeleteItemResponse res= DeleteItemResponse
   *       .builder()
   *       .attributes(attributes)
   *       .build();
   *       when(client.deleteItem(request)).thenReturn(res);
   *       assertEquals(false,dynamoDAO.deleteById(shortPath));
   *       }
   **/

  /**
   * @Test
   *       public void test2(){
   *       String shortPath="As";
   *       HashMap<String, AttributeValue> itemKey = new HashMap<>();
   *       itemKey.put(PK, AttributeValue.fromS(shortPath));
   *       var request= DeleteItemRequest.builder().tableName(TABLE_URL_ITEM)
   *       .key(itemKey)
   *       .returnValues(ReturnValue.ALL_OLD)
   *       .build();
   * 
   *       Map<String, AttributeValue> attributes = new HashMap<>();
   *       attributes.put(PK, fromS(shortPath));
   *       SdkHttpResponse resp =
   *       SdkHttpResponse.builder().statusCode(200).build();
   *       SdkResponse sdkResponse= DeleteItemResponse
   *       .builder()
   *       .sdkHttpResponse(resp)
   *       .build();
   * 
   * 
   *       DeleteItemResponse res = DeleteItemResponse.builder()
   *       when(client.deleteItem(request)).thenReturn(res);
   *       assertEquals(true,dynamoDAO.deleteById(shortPath));
   *       }
   */

}
