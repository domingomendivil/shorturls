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
public class DynamoDAO implements Query, Writer, Deleter {

	/*
	 * Dynamodb Table used for storing URL items
	 */
	protected static final String TABLE_URL_ITEM = "URLItem";

	/*
	 * Thread safe client
	 */
	private final DynamoDbClient client;

	/*
	 * Primary Key for the Dynamo Table, named "shortURL".
	 * Actually is the short code after the base URL.
	 * E.g. in http://me.li/SXJklas, it is "SXJklas"
	 */
	protected static final String PK = "shortURL";

	/*
	 * Long URL (original url) attribute in Dynamo Table
	 */
	protected static final String LONG_URL = "longURL";

	/*
	 * Creation date attribute of Dynamo Table
	 * in format "yyyy-MM-dd-HH-mm-ss."
	 */
	protected static final String CREATION_DATE = "creationDate";

	/*
	 * TTL (time to live) attribute
	 */
	protected static final String TTL = "ttl";

	public DynamoDAO(DynamoDbClient client) {
		this.client = client;
	}

	/**
	 * Gets a URLItem given its shortPath.
	 * If the URLItem is not found it returns an empty Optional
	 * Otherwise it returns an Optional with the URLItem.
	 */
	@Override
	public Optional<URLItem> getById(final String path) {
		if (path.equals("")) {
			throw new IllegalArgumentException();
		}
		val key = new HashMap<String, AttributeValue>();
		key.put(PK, fromS(path));
		val request = GetItemRequest.builder()
				.tableName(TABLE_URL_ITEM)
				.key(key)
				.build();
		val response = client.getItem(request);
		if (response.hasItem()) {
			return Optional.of(getURLItem(response));
		} else {
			return Optional.empty();
		}

	}

	/*
	 * Method for getting a URLIte from a Dynamo response.
	 * It parses the response
	 */
	private URLItem getURLItem(final GetItemResponse response) {
		try {
			val responseItem = response.item();
			val longURL = new URL(responseItem.get(LONG_URL).s());
			val shortPath = responseItem.get(PK).s();
			val str = responseItem.get(CREATION_DATE).s();
			val date = getDate(str);
			val time = responseItem.get(TTL);
			if (time != null) {
				val hours = time.n();
				long nro = Long.parseLong(hours);
				return new URLItem(shortPath, longURL, date, nro);
			} else {
				return new URLItem(shortPath, longURL, date, null);
			}
		} catch (MalformedURLException e) {
			throw new ShortURLRuntimeException(e);
		}
	}

	/*
	 * Parses a date in format "yyyy-MM-dd-HH-mm-ss."
	 */
	private LocalDateTime getDate(String s) {
		return LocalDateTime.parse(s);
	}

	/**
	 * Inserts a URLItem in DynamoDb. 
	 * In case the URLItem has an expirationTime (in seconds)
	 * it sets the ttl attribute. 
	 * The expirationTime is already in epoch UNIX time /seconds).
	 */
	@Override
	public void insert(final URLItem urlItem) {
		val item = new HashMap<String, AttributeValue>();
		item.put(PK, fromS(urlItem.getShortPath()));
		item.put(LONG_URL, fromS(urlItem.getLongURL().toString()));
		item.put(CREATION_DATE, fromS(urlItem.getCreationDate().toString()));
		val expirationTime = urlItem.getExpirationTime();
		if (expirationTime != null) {
			item.put(TTL, fromN(expirationTime.toString()));
		}
		val request = PutItemRequest.builder()
				.tableName(TABLE_URL_ITEM)
				.item(item).build();
		client.putItem(request);
	}

	/**
	 * Deletes a short URL given its shortPath (code). 
	 * If returns true if delete is successful, otherwise
	 * it returns false.
	 */
	@Override
	public boolean deleteById(final String shortPath) {
		val itemKey = new HashMap<String, AttributeValue>();
		itemKey.put(PK, AttributeValue.fromS(shortPath));
		val request = DeleteItemRequest.builder()
				.tableName(TABLE_URL_ITEM)
				.key(itemKey)
				.returnValues(ReturnValue.ALL_OLD)
				.build();
		val res = client.deleteItem(request);
		val hasAttributes = res.hasAttributes();
		return hasAttributes;
	}

}
