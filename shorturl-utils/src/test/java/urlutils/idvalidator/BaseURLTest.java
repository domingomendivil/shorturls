package urlutils.idvalidator;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BaseURLTest {
    
    @Test
    public void test1(){
        var b= new BaseURL("http://localhost:8080");
        assertEquals("http://localhost:8080/", b.toURL());
    }

    @Test
    public void test2(){
        var b= new BaseURL("http://localhost:8080/");
        assertEquals("http://localhost:8080/", b.toURL());
    }
}
