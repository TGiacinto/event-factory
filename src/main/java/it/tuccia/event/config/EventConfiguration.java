package it.tuccia.event.config;

import it.tuccia.event.engine.Event;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Map;

@Configuration
@Order(2)
public class EventConfiguration {


    @Bean(value = "eventsMap")
    public Map<String, Event> map(){
        return EventInitializer.map;
    }

}
