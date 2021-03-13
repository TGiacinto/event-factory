# Event-Factory

Event Factory is a springboot library for handling events.
Classes that implement the Event interface are loaded into a map whose value is inside the MessageHeaders interface with key event_name
## Usage

Add @EnableEventEngine annotations in the main spring class

```java
@EnableEventEngine(basePackageScan = "your.package.class")
```

Implement Event interface

```java
public class MyCustomEvent implements Event {

    @Override
    public void execute(String payloadBase64, MessageHeaders messageHeaders) {
        //TODO 
    }
}
```

The default event name is the class name written like this:
Example:

* Class: MyClassEvent
* Event: MY_CLASS_EVENT

If you want to set a custom event name, you can use the @MyEvent annotation

```java

@MyEvent(value = "MyCustomEventName")
public class MyCustomEvent implements Event {

    @Override
    public void execute(String payloadBase64, MessageHeaders messageHeaders) {
        //TODO 
    }
}

```

In your business class inject EventEngine class:

``` java
@Component
public class MyBusinessClass {


    @Autowired
    EventEngine eventEngine;

    public void myMethod() throws Exception {

        Map<String,Object> map = new HashMap<>();
        map.put("event_name","MY_EVENT");
        eventEngine.execute("",new MessageHeaders(map));

    }
}
```
