package deleteshorturl.dao;

import shorturls.cache.Cache;
import shorturls.dao.Deleter;

/**
 * Implementation of the Deleter interface that
 * deletes the short URL from the database, and
 * if the delete is successful it deletes the
 * item from the cache. 
 */
public final class DeleterDao implements Deleter{

	/**
	 * The deleter interface, you should pass a database implementation
	 * writer.
	 */
	private final Deleter deleter;
    
	/**
	 * The cache where to delete
	 */
	private final Cache cache;

    public DeleterDao(Deleter deleter, Cache cache){
        this.deleter=deleter;
        this.cache=cache;
    }

    @Override
    public boolean deleteById(String code) {
        if (deleter.deleteById(code)){
            cache.delete(code);
            return true;
        }
       return false;
    }
    
}
