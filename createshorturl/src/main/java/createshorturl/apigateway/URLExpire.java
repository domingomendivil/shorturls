package createshorturl.apigateway;


public class URLExpire {
    private String url;
    private Long seconds;
    

    public String getUrl(){
        return url;
    }
    public void setUrl(String url){
        this.url=url;
    }

    public Long getSeconds(){
        return seconds;
    }
    public void setSeconds(Long seconds){
        this.seconds=seconds;
    }
}
