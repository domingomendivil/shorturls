package deleteshorturl.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;

import shorturls.cache.Cache;
import shorturls.dao.Deleter;

@RunWith(MockitoJUnitRunner.class)
public class DeleterDAOTest {
	
	@InjectMocks
	private DeleterDao dao;
	
	@Mock
	private Deleter deleter;

	@Mock
	private Cache cache;
	
	@Test
	public void test1() {
		String code = "vhSUKX";
		when(dao.deleteById(code)).thenReturn(true);
		assertTrue(dao.deleteById(code));
	}
	
	@Test
	public void test2() {
		String code = "vhSUKX";
		when(dao.deleteById(code)).thenReturn(false);
		assertFalse(dao.deleteById(code));
	}
	

}
