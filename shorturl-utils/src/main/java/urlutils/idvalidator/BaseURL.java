package urlutils.idvalidator;

public class BaseURL {
    private final String url;

    public BaseURL(final String url){
    	if (!url.endsWith("/")) {
    		this.url=url+"/";	
    	}else {
    		this.url=url;
    	}
    }

    public String toURL(){
        return url;
    }
}
