package geturl.query;

import java.util.Optional;

import shorturls.cache.Cache;
import shorturls.dao.Query;
import shorturls.model.URLItem;

public class QueryWithOnlyCacheImpl implements Query{

    private final Cache cache;

    public QueryWithOnlyCacheImpl(Cache cache){
        this.cache=cache;
    }

    @Override
    public Optional<URLItem> getById(String path) {
        System.out.println("query only cache");
        return cache.getById(path);
    }
    
}
