package createshorturl.apigateway;

public final class URLExpire {
    private String url;
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
