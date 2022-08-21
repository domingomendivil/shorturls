package createshorturl.apigateway;

/**
 * Class used for converting the JSON input, when URLs with expiration
 * seconds are created. In this case Lombok was not used for generating 
 * getters and setters because problems with Jackson serialization.
 * 
 */
public final class URLExpire {
    /*
     * The URL itself to be associated with a short URL
     */
    private String url;
    /*
     * The seconds that the URL must exist. After these seconds 
     * have passed the short URL expires and it is automatically
     * deleted.
     */
    private Long seconds;
    

    public String getUrl(){
        return url;
    }

    public Long getSeconds(){
        return seconds;
    }
    public void setSeconds(Long seconds){
        this.seconds=seconds;
    }
}
