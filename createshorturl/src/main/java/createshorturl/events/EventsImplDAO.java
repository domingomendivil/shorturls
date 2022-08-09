package createshorturl.events;

import shorturls.dao.Writer;
import shorturls.model.URLItem;

public class EventsImplDAO implements Events{

	private final Writer dao;

	
	public EventsImplDAO(Writer dao){
		this.dao=dao;
	}

	@Override
	public void send(URLItem item) {
		dao.insert(item);
	}




}