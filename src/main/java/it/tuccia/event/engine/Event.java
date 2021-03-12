package it.tuccia.event.engine;

import org.springframework.messaging.MessageHeaders;

public interface Event {

    void execute(String payloadBase64, MessageHeaders messageHeaders);

}
