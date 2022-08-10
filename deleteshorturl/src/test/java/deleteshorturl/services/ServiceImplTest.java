package deleteshorturl.services;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import shorturls.dao.Deleter;
import urlutils.idvalidator.IdValidator;
import urlutils.idvalidator.ValidationException;

@RunWith(MockitoJUnitRunner.class)
public class ServiceImplTest {

    @InjectMocks
    private ServiceImpl svc;

    @Mock
    private IdValidator idValidator;

    @Mock
    private Deleter deleter;

    
    @Test(expected = InvalidArgumentsException.class)
    public void testDeleteShortURL2() throws MalformedURLException, InvalidArgumentsException, ValidationException{
    	URL url = new URL("http://www.montevideo.com.uy");
    	ValidationException e = new ValidationException();
    	when(idValidator.getCode(url)).thenThrow(e);
    	svc.deleteURL(new URL("http://www.montevideo.com.uy"));    
    }

    @Test
    public void testDeleteShortURL3() throws MalformedURLException, InvalidArgumentsException, ValidationException{
        String shortPath="FKSLC5S";
        URL url  = new URL("http://localhost:8000/FKSLC5S");
        when(idValidator.getCode(url)).thenReturn(shortPath);
        when(deleter.deleteById(shortPath)).thenReturn(true);
        assertTrue(svc.deleteURL(new URL("http://localhost:8000/FKSLC5S")));    
    }
    
    @Test
    public void testDeleteShortURL4() throws MalformedURLException, InvalidArgumentsException, ValidationException{
        String shortPath="FKSLC5S";
        URL url  = new URL("http://localhost:8000/FKSLC5S");
        when(idValidator.getCode(url)).thenReturn(shortPath);
        when(deleter.deleteById(shortPath)).thenReturn(false);
        assertFalse(svc.deleteURL(new URL("http://localhost:8000/FKSLC5S")));    
    }
    
    @Test
    public void testDeleteShortPath1() throws MalformedURLException, InvalidArgumentsException, ValidationException{
        String shortPath="FKSLC5S";
        when(idValidator.isValid(shortPath)).thenReturn(true);
        when(deleter.deleteById(shortPath)).thenReturn(false);
        assertFalse(svc.deleteURL(shortPath));    
    }
    
    @Test
    public void testDeleteShortPath2() throws MalformedURLException, InvalidArgumentsException, ValidationException{
        String shortPath="FKSLC5S";
        when(idValidator.isValid(shortPath)).thenReturn(true);
        when(deleter.deleteById(shortPath)).thenReturn(true);
        assertTrue(svc.deleteURL(shortPath));    
    }
    
    @Test(expected = InvalidArgumentsException.class)
    public void testDeleteShortPath3() throws MalformedURLException, InvalidArgumentsException, ValidationException{
        String shortPath="FKSLC5S";
        when(idValidator.isValid(shortPath)).thenReturn(false);
        svc.deleteURL(shortPath);    
    }
    
    
    
    
    
    
    
    
    
    


}
