package it.tuccia.event.engine;

import it.tuccia.event.exception.EventEngineException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;

@CommonsLog
@Component
public class EventEngine {

    private static final String EVENT_NAME = "event_name";

    @Autowired
    @Qualifier(value = "eventsMap")
    Map<String,Event> mapEvents;

    public void execute(String payload, MessageHeaders messageHeaders) throws EventEngineException {

        try {
            String eventName = (String)messageHeaders.get(EVENT_NAME);
            Assert.notNull(eventName,"Event name must not be null");
            Event event = mapEvents.get(eventName);
            if(event != null){
                log.info("Event ".concat(eventName).concat(" found."));
                event.execute(payload,messageHeaders);
            }else {
                log.info("Event ".concat(eventName).concat(" not found."));
            }

        }catch (Throwable e){
            throw new EventEngineException(e);
        }

    }




}
