package com.meli.dynamo;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import shorturls.config.ShortURLProperties;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

public class DynamoDbClientFactoryTest {
	

	
	@Test
	public void test1() {
		ShortURLProperties props =  mock(ShortURLProperties.class);
		when(props.getProperty("DYNAMO_URL")).thenReturn(null);
		DynamoDbAsyncClient client = DynamoDbClientFactory.getClient(props);
		assertNotNull(client);
	}
	
	@Test
	public void test2() {
		ShortURLProperties props = mock(ShortURLProperties.class);
		when(props.getProperty("DYNAMO_URL")).thenReturn("");
		DynamoDbAsyncClient client = DynamoDbClientFactory.getClient(props);
		assertNotNull(client);
	}
	
    
}
