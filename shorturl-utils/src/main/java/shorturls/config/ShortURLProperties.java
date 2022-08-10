package shorturls.config;


public class ShortURLProperties {
    public String getProperty(String name){
        String prop =System.getenv(name);
        if ((prop ==null ) || (prop.equals(""))){
        	throw new ConfigurationException(String.format("Property % has not been set",name));
        }
        return prop;
    }
}
