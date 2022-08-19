package shorturls.dao;

/**
 * Deleter interface for deleting a short URL.
 * 
 *
 */
public interface Deleter {
	
	/**
	 * Deletes a short URL identified by the code (shortPath)
	 * that comes after the base url.
	 * E.g. in http://me.li/324kxS the code that identifies
	 * the short URL is "324kxS"
	 * @param shortPath the code that identifies the URL
	 * @return
	 */
    public boolean deleteById(String shortPath);
}
