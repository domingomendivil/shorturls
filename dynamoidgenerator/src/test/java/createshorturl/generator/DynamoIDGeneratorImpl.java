package createshorturl.generator;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;

import org.junit.Test;

import shorturls.dynamodao.DynamoDAO;
import shorturls.model.URLItem;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoIDGeneratorImpl {


    private static synchronized DynamoDbClient getDynamoDBClient() {
           return  DynamoDbClient
                .builder().
                endpointOverride(URI.create("http://localhost:8000")).build();
           
    }

    @Test
    public void test1() throws MalformedURLException{
        URLItem urlItem = new URLItem();
        urlItem.setShortPath("j");
        urlItem.setCreationDate(LocalDateTime.now());
        urlItem.setExpirationHours(1L);
        urlItem.setLongURL(new URL("http://www.montevideo.com.uy"));    
        new DynamoDAO(getDynamoDBClient()).insert(urlItem);
    }

}
