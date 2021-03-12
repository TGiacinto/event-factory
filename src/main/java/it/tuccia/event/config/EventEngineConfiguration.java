package it.tuccia.event.config;

import it.tuccia.event.engine.EventEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(3)
public class EventEngineConfiguration {


    @Bean
    public EventEngine eventEngine(){
        return new EventEngine();
    }

}
