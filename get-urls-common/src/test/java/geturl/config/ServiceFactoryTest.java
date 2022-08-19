package geturl.config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.meli.factory.Factory;

import shorturls.config.ConfigurationException;
import shorturls.config.ShortURLProperties;
import shorturls.constants.Constants;

@RunWith(MockitoJUnitRunner.class)
public class ServiceFactoryTest {
	
	@Mock
	private ShortURLProperties properties;
	
	
	@Test(expected=ConfigurationException.class)
	public void test1() {
		ConfigurationException e = new ConfigurationException("");
		when(properties.getProperty("CACHE_ENABLED")).thenThrow(e );
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


}
