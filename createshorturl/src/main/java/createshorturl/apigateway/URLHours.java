package createshorturl.apigateway;


public class URLHours {
    
    /*
     * The URL itself to be associated with a short URL
     */
    private String url; 

    /*
     * The hours that the URL must exist. After these hours 
     * have passed the short URL expires and it is automatically
     * deleted.
     */
    private Long hours;

    public String getUrl(){
        return this.url;
    }

    public void setUrl(String url){
        this.url=url;
    }

    public Long getHours(){
        return this.hours;
    }
}
