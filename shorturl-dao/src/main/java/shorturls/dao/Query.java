package shorturls.dao;

import java.util.Optional;

import shorturls.model.URLItem;

public interface Query {
    public Optional<URLItem> getById(String path);
}
