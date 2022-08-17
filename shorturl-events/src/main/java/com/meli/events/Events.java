package com.meli.events;

import java.util.Map;

/**
 * Events interface used for sending events. 
 */
public interface Events {
	
	
	/**
	 * Sends a message of properties in a map, which is associated
	 * with a specific key.
	 * @param key the key associated with the message
	 * @param message the message to send
	 */
	public void send(String key,Map<String,String> message);
	


}
