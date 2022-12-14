package shorturls.cache;

import java.util.Optional;

import shorturls.model.URLItem;
/**
 * Cache interface for getting the URLs
 *
 */
public interface Cache {
	
	public Optional<URLItem> getById(String path);
	
	public void put(String path,URLItem urlItem);
    
	public boolean delete(String path);
}