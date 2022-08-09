package deleteshorturl.dao;

import shorturls.cache.Cache;
import shorturls.dao.Deleter;

public class DeleterDao implements Deleter{

    private final Deleter deleter;
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
