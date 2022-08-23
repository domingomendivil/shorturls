package createshorturl.events;

import shorturls.dao.Writer;
import shorturls.model.URLItem;

/**
 * Class that implements the Events interface. 
 * Actually the only thing that does is 
 * write the the event of sending the URLItem
 * to the database layer.
 * 
 *
 */
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
