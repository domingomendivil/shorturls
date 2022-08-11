package geturl.config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.meli.events.Events;
import com.meli.factory.Factory;

import shorturls.cache.Cache;
import shorturls.config.ConfigurationException;
import shorturls.config.ShortURLProperties;
import shorturls.constants.Constants;
import shorturls.dao.Query;

@RunWith(MockitoJUnitRunner.class)
public class ServiceFactoryTest {
	
	@Mock
	private ShortURLProperties properties;
	
/**	@Test
	public void test0() {
		when(properties.getProperty("DYNAMO_URL")).thenReturn("http://dynamo-local:8000");
		when(properties.getProperty(Constants.CACHE_ENABLED)).thenReturn("false");
		when(properties.getProperty(Constants.QUERY_FACTORY)).thenReturn("shorturls.dynamodao.DynamoDAOFactory");
		when(properties.getProperty(Constants.BASE_URL)).thenReturn("http://127.0.0.1:3000");
		when(properties.getProperty(Constants.EVENTS_FACTORY)).thenReturn("com.meli.kafka.KafkaFactory");
		when(properties.getProperty(Constants.RANDOM_ALPHABET)).thenReturn("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
		when(properties.getProperty("KAFKA_ENABLED")).thenReturn("false");

		ServiceFactory.init(properties,new Factory<Query>(),new Factory<Cache>(),new Factory<Events>());
	}**/
	
	@Test(expected=ConfigurationException.class)
	public void test1() {
		ConfigurationException e = new ConfigurationException("");
		when(properties.getProperty("CACHE_ENABLED")).thenThrow(e );

		/**
		Factory factory1 = Mockito.mock(Factory.class);
		Factory factory2 = Mockito.mock(Factory.class);
		Factory factory3 = Mockito.mock(Factory.class);
**/
		ServiceFactory.init(properties,null,null,null);
	}
	
	@Test(expected=ConfigurationException.class)
	public void test2() {
		ConfigurationException e = new ConfigurationException("");
		when(properties.getProperty(Constants.CACHE_ENABLED)).thenReturn("true");
		when(properties.getProperty(Constants.QUERY_FACTORY)).thenThrow(e);
		ServiceFactory.init(properties,null,null,null);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Test(expected=ConfigurationException.class)
	public void test3() {
		when(properties.getProperty(Constants.CACHE_ENABLED)).thenReturn("true");
		when(properties.getProperty(Constants.QUERY_FACTORY)).thenReturn("badFactoryClass");
		@SuppressWarnings("rawtypes")
		Factory factory =mock(Factory.class);
		com.meli.factory.ConfigurationException e = new com.meli.factory.ConfigurationException("");
		when(factory.getInstance("badFactoryClass")).thenThrow(e);
		ServiceFactory.init(properties,factory,null,null);
	}
	
	
	/*@Test(expected=ConfigurationException.class)
	public void test4() {
		when(properties.getProperty(Constants.CACHE_ENABLED)).thenReturn("true");
		when(properties.getProperty(Constants.QUERY_FACTORY)).thenReturn("shorturls.dynamodao.DynamoDAOFactory");
		when(properties.getProperty(Constants.CACHE_FACTORY)).thenReturn("badFactoryClass");
		
		ServiceFactory.init(properties);
	}

	@Test(expected=ConfigurationException.class)
	public void test5() {
		when(properties.getProperty(Constants.CACHE_ENABLED)).thenReturn("true");
		when(properties.getProperty(Constants.QUERY_FACTORY)).thenReturn("shorturls.dynamo.DynamoDAOFactory");
		when(properties.getProperty(Constants.CACHE_FACTORY)).thenReturn("shorturl.cache.RedisCacheFactory");
		ServiceFactory.init(properties);
	}**/

}
