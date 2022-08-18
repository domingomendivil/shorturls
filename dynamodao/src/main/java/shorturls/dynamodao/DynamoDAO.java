package shorturls.dynamodao;

import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromN;
import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromS;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import lombok.val;
import shorturls.dao.Deleter;
import shorturls.dao.Query;
import shorturls.dao.QueryException;
import shorturls.dao.Writer;
import shorturls.model.URLItem;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;

/**
 * Data access implementing the three interfaces for
 * CRUD operations of URL items over DynamoDB 
 */
public class DynamoDAO implements Query,Writer,Deleter {

	/*
	 * Dynamodb Table used for storing URL items
	 */
	protected static final String TABLE_URL_ITEM = "URLItem";

	/*
	 * Thread safe client
	 */
	private final DynamoDbAsyncClient client;
	
	protected static final String PK="shortURL";
	protected static final String LONG_URL="longURL";
	protected static final String CREATION_DATE="creationDate";
	protected static final String TTL = "ttl";
	protected static final String EXPIRATION_TIME = "expirationHours";

    public DynamoDAO(DynamoDbAsyncClient client){
        this.client=client;
    }

    @Override
	public Optional<URLItem> getById(String path) {
		if (path.equals("")){
			throw new IllegalArgumentException();
		}
		val key = new HashMap<String, AttributeValue>();
		key.put(PK, fromS(path));
		GetItemRequest request = GetItemRequest.builder()
		.tableName(TABLE_URL_ITEM)
		.key(key)
		.build();
		try {
			val response = client.getItem(request).get();
			if (response.hasItem()){
				return Optional.of(getURLItem(response));
			}else{
				return Optional.empty();
			}
		} catch (InterruptedException|ExecutionException e) {
			return Optional.empty();
		} 
		
	}

	private URLItem getURLItem(GetItemResponse response) {
		//val urlItem = new URLItem();
		try {
			val responseItem = response.item();
			val longURL = new URL(responseItem.get(LONG_URL).s());
			val shortPath = responseItem.get(PK).s();
			val str=responseItem.get(CREATION_DATE).s();
			val date = getDate(str);
			val time = responseItem.get(EXPIRATION_TIME);
			if (time !=null) {
				val hours =time.n();
				long nro = Long.parseLong(hours);
				return new URLItem(shortPath,longURL,date,nro);
			}else {
				return new URLItem(shortPath,longURL,date,null);
			}
		} catch (MalformedURLException e) {
			throw new QueryException(e);
		}
	}
	
	private LocalDateTime getDate(String s) {
		return LocalDateTime.parse(s);
	}

	@Override
	public void insert(URLItem urlItem) {
		val item = new HashMap<String, AttributeValue>();
		item.put(PK,  fromS(urlItem.getShortPath()));
		item.put(LONG_URL,  fromS(urlItem.getLongURL().toString()));
		item.put(CREATION_DATE,  fromS(urlItem.getCreationDate().toString()));
		val expirationTime= urlItem.getExpirationTime();
		if (expirationTime!=null){
			item.put(EXPIRATION_TIME,fromN(expirationTime.toString()));
			Long ttl = expirationTime;
		    item.put(TTL,fromN(ttl.toString()));
		}
		val request = PutItemRequest.builder()
				.tableName(TABLE_URL_ITEM)
				.item(item).build();
		System.out.println("put item");
		try {
			var res=client.putItem(request).get();
			System.out.println(res.sdkHttpResponse().isSuccessful());
		} catch (InterruptedException|ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	@Override	
	public boolean deleteById(String shortPath) {
		val itemKey = new HashMap<String, AttributeValue>();
		itemKey.put(PK, AttributeValue.fromS(shortPath));
		val request = DeleteItemRequest.builder()
				.tableName(TABLE_URL_ITEM)
				.key(itemKey)
				.returnValues(ReturnValue.ALL_OLD)
				.build(); 
		val res = client.deleteItem(request);
		try {
			int nro = res.get().sdkHttpResponse().statusCode();
			return nro==200;
		} catch (InterruptedException|ExecutionException e) {
			return false;
		} 
	}
	
	public static void main(String[] args) {
		System.out.println(LocalDateTime.now());
	}
    
}
