package geturl.query;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import shorturls.cache.Cache;
import shorturls.dao.Query;
import shorturls.model.URLItem;
@RunWith(MockitoJUnitRunner.class)
public class QueryWithCacheImplTest {
    
    @InjectMocks
    private QueryWithCacheImpl queryCache;

    @Mock
    private Query query; 

    @Mock
    private Cache cache;

    @Test
    public void test1() throws MalformedURLException{
        when(query.getById("A")).thenReturn(null);
        URLItem urlItem = new URLItem();
        urlItem.setLongURL(new URL("http://www.google.com"));
        Optional<URLItem> item = Optional.of(urlItem);
        when(cache.getById("A")).thenReturn(item);
        var res = queryCache.getById("A");
        assertEquals(item,res);
    }
}
