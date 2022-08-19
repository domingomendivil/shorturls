package geturl.query;

import java.util.Optional;

import lombok.val;
import shorturls.cache.Cache;
import shorturls.dao.Query;
import shorturls.model.URLItem;

/**
 * Class for querying cache first and then the database
 */
public class QueryWithCacheImpl implements Query{

	private final  Query query;
	private final Cache cache;
	
	public QueryWithCacheImpl(Query query,Cache cache) {
		this.query = query;
		this.cache = cache;
	}
	
	@Override
	public Optional<URLItem> getById(String path) {
		Optional<URLItem> urlItem =cache.getById(path);
		if (urlItem.isEmpty()) {
			val item = query.getById(path);
			if (item.isPresent()) {
				cache.put(path, item.get());
			}
			return item;
		}else {
			return urlItem;
		}
	}

}
