package com.meli.dynamo;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import lombok.val;
import shorturls.config.ShortURLProperties;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Unit Testing class for DynamoDbClientFactory class
 */
public class DynamoDbClientFactoryTest {
	
	
	@Test
	public void test1() {
		val props =  mock(ShortURLProperties.class);
		when(props.getProperty("DYNAMO_URL")).thenReturn(null);
		val client = DynamoDbClientFactory.getClient(props);
		assertNotNull(client);
	}
	
	@Test
	public void test2() {
		val props = mock(ShortURLProperties.class);
		when(props.getProperty("DYNAMO_URL")).thenReturn("");
		val client = DynamoDbClientFactory.getClient(props);
		assertNotNull(client);
	}
	
    
}
