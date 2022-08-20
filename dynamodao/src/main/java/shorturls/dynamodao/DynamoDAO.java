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
import shorturls.dao.Writer;
import shorturls.exceptions.ShortURLRuntimeException;
import shorturls.model.URLItem;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
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
	private final DynamoDbClient client;
	
	protected static final String PK="shortURL";
	protected static final String LONG_URL="longURL";
	protected static final String CREATION_DATE="creationDate";
	protected static final String TTL = "ttl";

    public DynamoDAO(DynamoDbClient client){
        this.client=client;
    }

    @Override
	public Optional<URLItem> getById(String path) {
		System.out.println("dynamodao getById "+path);
		if (path.equals("")){
			System.out.println("path vacia");
			throw new IllegalArgumentException();
		}
		val key = new HashMap<String, AttributeValue>();
		key.put(PK, fromS(path));
		GetItemRequest request = GetItemRequest.builder()
		.tableName(TABLE_URL_ITEM)
		.key(key)
		.build();
		System.out.println("dynamodao luego de armar request");
			val response = client.getItem(request);
			System.out.println("dynamodao client.getItem(request).get");
			if (response.hasItem()){
				System.out.println("hasitem");

				return Optional.of(getURLItem(response));
			}else{
				System.out.println("hasno item");
				return Optional.empty();
			}
		
	}

	private URLItem getURLItem(GetItemResponse response) {
		System.out.println("geturlitem");
		try {
			val responseItem = response.item();
			System.out.println("response.item()");
			val longURL = new URL(responseItem.get(LONG_URL).s());
			val shortPath = responseItem.get(PK).s();
			val str=responseItem.get(CREATION_DATE).s();
			val date = getDate(str);
			val time = responseItem.get(TTL);
			if (time !=null) {
				System.out.println("time nonnull");
				val hours =time.n();
				long nro = Long.parseLong(hours);
				System.out.println("retorno urlitem" );
				return new URLItem(shortPath,longURL,date,nro);
			}else {
				System.out.println("retorno urlitem sin tiempo");
				return new URLItem(shortPath,longURL,date,null);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new ShortURLRuntimeException(e);
		}
	}
	
	private LocalDateTime getDate(String s) {
		return LocalDateTime.parse(s);
	}

	@Override
	public void insert(URLItem urlItem) {
		System.out.println("dynamodao insert");
		val item = new HashMap<String, AttributeValue>();
		item.put(PK,  fromS(urlItem.getShortPath()));
		item.put(LONG_URL,  fromS(urlItem.getLongURL().toString()));
		item.put(CREATION_DATE,  fromS(urlItem.getCreationDate().toString()));
		val expirationTime= urlItem.getExpirationTime();
		if (expirationTime!=null){
		    item.put(TTL,fromN(expirationTime.toString()));
		}
		val request = PutItemRequest.builder()
				.tableName(TABLE_URL_ITEM)
				.item(item).build();
			client.putItem(request);
			System.out.println("dynamodao.insert final ok");
	}

	@Override	
	public boolean deleteById(String shortPath) {
		System.out.println("deletebyid dynamodaos");
		val itemKey = new HashMap<String, AttributeValue>();
		itemKey.put(PK, AttributeValue.fromS(shortPath));
		val request = DeleteItemRequest.builder()
				.tableName(TABLE_URL_ITEM)
				.key(itemKey)
				.returnValues(ReturnValue.ALL_OLD)
				.build(); 
		val res = client.deleteItem(request);
			val hasAttributes= res.hasAttributes();
			System.out.println("delete dynammodao "+hasAttributes);
			return hasAttributes;
	}

    
}
