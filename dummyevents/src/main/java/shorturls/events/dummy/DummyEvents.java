package shorturls.events.dummy;

import com.meli.events.Events;
import java.util.Map;

/**
 * This class was created to simulate sending events.
 * Instead of actually sending events to a Event stream
 * service, it just do nothing. 
 */
public class DummyEvents implements Events {

    @Override
    public void send(String key,Map<String,String> message){
        //do nothing
    }

    public static final DummyEvents getInstance(){
        return new DummyEvents();
    }
    
}
