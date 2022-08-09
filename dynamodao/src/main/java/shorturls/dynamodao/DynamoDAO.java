package shorturls.dynamodao;

import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromN;
import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromS;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import lombok.val;
import shorturls.dao.Deleter;
import shorturls.dao.Query;
import shorturls.dao.QueryException;
import shorturls.dao.Writer;
import shorturls.model.URLItem;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;


public class DynamoDAO implements Query,Writer,Deleter {

	private static final String TABLE_URL_ITEM = "URLItem";

	private final DynamoDbClient client;
	
	private static final String PK="shortURL";
	private static final String LONG_URL="longURL";
	private static final String CREATION_DATE="creationDate";
	private static final String TTL = "ttl";
	private static final String EXPIRATION_HOURS = "expirationHours";

    public DynamoDAO(DynamoDbClient client){
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
		
		val response = client.getItem(request);
		if (response.hasItem()) {
			return Optional.of(getURLItem(response));
		}else{
			return Optional.empty();
		}
	}

	private URLItem getURLItem(GetItemResponse response) {
		//val urlItem = new URLItem();
		try {
			val responseItem = response.item();
			val longURL = new URL(responseItem.get(LONG_URL).s());
			//urlItem.setLongURL(longURL);
			val shortPath = responseItem.get(PK).s();
			//urlItem.setShortPath(responseItem.get(PK).s());
			val str=responseItem.get(CREATION_DATE).s();
			val date = getDate(str);
			//urlItem.setCreationDate(date);
			val hoursItem = responseItem.get(EXPIRATION_HOURS);
			if (hoursItem !=null) {
				val hours =hoursItem.n();
				long nro = Long.parseLong(hours);
				//urlItem.setExpirationHours(nro);
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
		val expirationHours= urlItem.getExpirationHours();
		if (expirationHours!=null){
			item.put(EXPIRATION_HOURS,fromN(expirationHours.toString()));
			//TTL in Dynamodb is expressed in epoch time
			//The hours must be converted to miliseconds and then 
			//the current time must be added to get the new epoch time
			Long ttl = System.currentTimeMillis() +(3600000*expirationHours);
		    item.put(TTL,fromN(ttl.toString()));
		}
		val request = PutItemRequest.builder()
				.tableName(TABLE_URL_ITEM)
				.item(item).build();
		client.putItem(request);
	}

	@Override
	public boolean deleteById(String shortPath) {
		val itemKey = new HashMap<String, AttributeValue>();
		itemKey.put(PK, AttributeValue.fromS(shortPath));
		DeleteItemRequest request = DeleteItemRequest.builder()
				.tableName(TABLE_URL_ITEM)
				.key(itemKey)
				.returnValues(ReturnValue.ALL_OLD)
				.build(); 
		val res = client.deleteItem(request);
		res.sdkHttpResponse().statusCode();
		return res.sdkHttpResponse().statusCode()==200;
	}
    
}
