package shorturls.config;


/**
 * Class implementing ShortURLProperties.
 * Properties are obtained from the system environment
 * 
 */
public class ShortURLPropertiesImpl implements ShortURLProperties{
    
	@Override
	public String getProperty(String name){
        return System.getenv(name);
    }
}
