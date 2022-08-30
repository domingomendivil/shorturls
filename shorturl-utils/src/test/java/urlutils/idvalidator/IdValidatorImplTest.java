package urlutils.idvalidator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class IdValidatorImplTest {

	private static final String BASE62_ALPHABET = "^[a-zA-Z0-9]*$";

	private IdValidatorImpl getIdValidator(String url,int length){
		BaseURL baseURL = new BaseURL(url);
		return new IdValidatorImpl(baseURL, BASE62_ALPHABET,length);
	}

	@ParameterizedTest
	@ValueSource(strings= {"","/","&"})
	public void testIsValid1(String arg) {
		var validator=getIdValidator("http://localhost:8080",1);
		assertFalse(validator.isValid(arg));
	}
	
	@Test
	public void testIsValid2() {
		var validator=getIdValidator("http://localhost:8080",1);
		assertFalse(validator.isValid(""));
	}
	
	
	@ParameterizedTest
	@ValueSource(strings= {"AB","zy","A1","BA"})
	public void testIsValid5(String arg) {
		var validator=getIdValidator("http://localhost:8080",2);
		assertTrue(validator.isValid(arg));
	}
	
	@ParameterizedTest
	@ValueSource(strings= {"","A&","ABC","a","A","A#"})
	public void testIsValid6(String arg) {
		var validator=getIdValidator("http://localhost:8080",2);
		assertFalse(validator.isValid(arg));
	}
	

	@ParameterizedTest
	@ValueSource(strings= {
	"http://localhost:8080/a"
	,"http://127.0.0.1:8080/a"
	,"http://127.0.0.1:8080/a/a"
	,"http://127.0.0.1:8080/a/b/a"})
	public void testGetCode6(String arg) throws MalformedURLException, ValidationException {
		var validator=getIdValidator("http://localhost:8080",1);
		String code = validator.getCode(new URL(arg));
		assertEquals("a",code);
	}
	
	@ParameterizedTest
	@ValueSource(strings= {
	"http://localhost:8080/a"
	,"http://127.0.0.1:8080/a"
	,"http://127.0.0.1:8080/a/a"
	,"http://127.0.0.1:8080/a/b/a"})
	public void testGetCode7(String arg) throws MalformedURLException, ValidationException {
		var validator=getIdValidator("http://localhost:8080/",1);
		String code = validator.getCode(new URL(arg));
		assertEquals("a",code);
	}
	

	
	@ParameterizedTest
	@ValueSource(strings= {
	"https://localhost:8080/a"
	,"https://localhost:8080/a/a"
	,"https://localhost:8080/a/a/a"
	,"https://127.0.0.1:8080/a"
	,"https://127.0.0.1:8080/a/a"
	,"https://127.0.0.1:8080/b/a"})
	public void testGetCode9(String arg) throws MalformedURLException, ValidationException {
		var validator = getIdValidator("https://127.0.0.1:8080/",1);
		String code = validator.getCode(new URL(arg));
		assertEquals("a",code);
	}

	@ParameterizedTest
	@ValueSource(strings= {
		"https://localhost:8080/a/a"
		,"https://localhost:8080/a/b"
		,"https://localhost:8080/a/#"
		,"http://localhost:8080/a&"
		,"http://localhost:8080/"
		,"http://localhost:8080"
		,"https://127.0.0.1:8080/a"
		,"http://127.0.0.1:8080/a"
		,"http://127.0.0.1:8080/ab"
		,"http://127.0.0.1:8080/abcd"
		,"http://localhost:8080/abcd"
		,"http://localhost:8080/a"
		,"http://localhost:8080/ab"})
	public void testGetCode11(String arg) throws MalformedURLException {
		var validator=getIdValidator("http://localhost:8080",3);
		boolean exception=false;
		try {
			validator.getCode(new URL(arg));
		} catch (ValidationException e) {
			exception=true;
		}
		assertTrue(exception);
	}
	

}
