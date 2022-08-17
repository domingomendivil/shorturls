package shorturls.config;

import java.util.ResourceBundle;

public class ShortURLProperties {
    public String getProperty(String name){
        return System.getenv(name);
    }
    
   
}
