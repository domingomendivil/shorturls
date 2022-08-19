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
			System.out.println("query empty db");
			var item = query.getById(path);
			if (item.isPresent()) {
				System.err.println("query en cache is present");
				cache.put(path, item.get());
			}
			return item;
		}else {
			System.out.println("query en db existe");
			return urlItem;
		}
	}

}
