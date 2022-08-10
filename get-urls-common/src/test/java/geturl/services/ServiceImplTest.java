package geturl.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.meli.events.Events;

import shorturls.dao.Query;
import shorturls.exceptions.InvalidArgumentException;
import shorturls.model.URLItem;
import urlutils.idvalidator.IdValidator;
import urlutils.idvalidator.ValidationException;

@RunWith(MockitoJUnitRunner.class)
public class ServiceImplTest {

    @InjectMocks
    private ServiceImpl svc;

    @Mock
    private IdValidator idValidator;

    @Mock
    private Query query;
	
    @Mock
    private Events events;


    @Test
    public void testGetLongURL1() throws MalformedURLException, InvalidArgumentException, ValidationException{
    	URL url = new URL("http://www.montevideo.com.uy");
    	var urlResponse = new URL("http://www.google.com");
    	when(idValidator.getCode(url)).thenReturn("ASF");
    	URLItem item = new URLItem("ASF",urlResponse,LocalDateTime.now(),null);
		when(query.getById("ASF")).thenReturn(Optional.of(item));
        var res = svc.getLongURL(url,null);
        assertEquals(urlResponse,res.get());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testGetLongURL2() throws MalformedURLException, InvalidArgumentException, ValidationException{
    	URL url = new URL("http://www.montevideo.com.uy");
    	ValidationException e = new ValidationException();
		when(idValidator.getCode(url)).thenThrow(e);
        svc.getLongURL(url,null);    
    }

/**    @Test(expected = InvalidArgumentException.class)
    public void testGetLongURL3() throws MalformedURLException, InvalidArgumentException{
        svc.getURL(new URL("http://www.montevideo.com.uy/index.html"),null);    
    }

    @Test(expected = InvalidArgumentException.class)
    public void testGetLongURL4() throws MalformedURLException, InvalidArgumentException{
        svc.getURL(new URL("file:///sdcard"),null);    
    }

    @Test(expected = InvalidArgumentException.class)
    public void testGetLongURL5() throws MalformedURLException, InvalidArgumentException{
        svc.getURL(new URL("file:///sdcard"),null);    
    }**/

   
}
