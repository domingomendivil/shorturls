package createshorturl.events;

import shorturls.model.URLItem;


public interface Events {
	
	public void send(URLItem item);
	


}
