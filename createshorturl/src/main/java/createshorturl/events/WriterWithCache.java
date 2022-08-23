package createshorturl.events;

import shorturls.cache.Cache;
import shorturls.dao.Writer;
import shorturls.model.URLItem;

public class WriterWithCache implements Writer{
	
	private final Writer writer;
	
	private final Cache cache;

	
	public WriterWithCache(Writer writer,Cache cache) {
		this.writer=writer;
		this.cache=cache;
	}

	@Override
	public void insert(final URLItem urlItem) {
		writer.insert(urlItem);
		cache.put(urlItem.getShortPath(), urlItem);
	}

}
