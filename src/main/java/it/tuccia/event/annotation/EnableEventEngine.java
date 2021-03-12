package it.tuccia.event.annotation;

import it.tuccia.event.config.EventConfiguration;
import it.tuccia.event.config.EventEngineConfiguration;
import it.tuccia.event.config.EventInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({
        EventInitializer.class,
        EventConfiguration.class,
        EventEngineConfiguration.class
})
@Component
public @interface EnableEventEngine {

    String basePackageScan();

}
