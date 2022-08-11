package shorturls.config;


public class ShortURLProperties {
    public String getProperty(String name){
        return System.getenv(name);
    }
}
