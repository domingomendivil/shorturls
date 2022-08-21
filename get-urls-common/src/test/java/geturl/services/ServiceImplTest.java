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

import lombok.val;
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

    private void whenValidateURLThroException(String urlStr) throws MalformedURLException, ValidationException{
    	val url = new URL(urlStr);
    	val e = new ValidationException();
		when(idValidator.getCode(url)).thenThrow(e);
    }

    private void whenQueryReturn(String shortPath,String url,LocalDateTime date,Long seconds) throws MalformedURLException{
    	val aURL = new URL(url);
    	val item = new URLItem(shortPath,aURL,date,seconds);
		when(query.getById(shortPath)).thenReturn(Optional.of(item));
    }

    @Test
    public void testGetLongURL1() throws MalformedURLException, InvalidArgumentException, ValidationException{
    	val url = new URL("http://www.montevideo.com.uy");
    	val urlResponse = new URL("http://www.google.com");
    	when(idValidator.getCode(url)).thenReturn("ASF");
    	whenQueryReturn("ASF","http://www.google.com",LocalDateTime.now(),null);
		val res = svc.getLongURL(url,null);
        assertEquals(urlResponse,res.get());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testGetLongURL2() throws MalformedURLException, InvalidArgumentException, ValidationException{
        String url = "http://www.montevideo.com.uy";
        whenValidateURLThroException(url);
        svc.getLongURL(new URL(url),null);    
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testGetLongURL3() throws MalformedURLException, InvalidArgumentException, ValidationException{
    	val url = new URL("http://www.montevideo.com.uy");
        whenValidateURLThroException("http://www.montevideo.com.uy");
        svc.getLongURL(url,null);    
    }

    @Test(expected = InvalidArgumentException.class)
    public void testGetURL1() throws MalformedURLException, InvalidArgumentException{
    	when(idValidator.isValid("code")).thenReturn(false);
    	svc.getURL("code",null);    
    }
    
    @Test
    public void testGetURL2() throws MalformedURLException, InvalidArgumentException{
    	when(idValidator.isValid("code")).thenReturn(true);
    	whenQueryReturn("code","http://www.google.com",LocalDateTime.now(),1L);
    	URL url = new URL("http://www.google.com");
    	val response =svc.getURL("code",null);    
    	assertEquals(Optional.of(url),response);
    }
    
    @Test
    public void testGetURL3() throws MalformedURLException, InvalidArgumentException{
    	when(idValidator.isValid("code")).thenReturn(true);
    	when(query.getById("code")).thenReturn(Optional.empty());
    	val response =svc.getURL("code",null);    
    	assertEquals(Optional.empty(),response);
    }
  
}
