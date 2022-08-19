package shorturls.dao;

import java.util.Optional;

import shorturls.model.URLItem;

/**
 * 
 * 
 *
 */
public interface Query {
   
	/**
	 * 
	 * @param path
	 * @return
	 */
	public Optional<URLItem> getById(String path);
}
