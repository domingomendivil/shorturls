package shorturls.events.dummy;

import com.meli.events.Events;
import java.util.Map;

public class DummyEvents implements Events {

    @Override
    public void send(String key,Map<String,String> message){
        //do nothing
    }

    public DummyEvents getInstance(){
        return new DummyEvents();
    }
    
}
