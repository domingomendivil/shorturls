package geturl.query;

import java.util.Optional;

import shorturls.cache.Cache;
import shorturls.dao.Query;
import shorturls.model.URLItem;


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
			var item = query.getById(path);
			if (item.isPresent()) {
				cache.put(path, item.get());
			}
			return item;
		}else {
			return urlItem;
		}
	}

}
