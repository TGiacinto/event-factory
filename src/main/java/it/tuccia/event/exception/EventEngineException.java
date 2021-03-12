package it.tuccia.event.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EventEngineException extends Exception{

    public EventEngineException(Throwable e){
        super(e);
    }

}
