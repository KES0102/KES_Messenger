package org.example.springclasses.controller;

import org.example.springclasses.models.Person1Person2MSG;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class SseController {
    public List<SseEmitter> getEmitters() {
        return emitters;
    }

    public List<SseEmitter> emitters=new CopyOnWriteArrayList<>();

    @Async
    @CrossOrigin
    @GetMapping(path ="/stream-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamSse() throws IOException {

        SseEmitter sseEmitter=new SseEmitter(Long.MAX_VALUE);
        sseEmitter.send(SseEmitter.event().name("SUBSCRIBE"));
        emitters.add(sseEmitter);
        System.out.println("emitters.size()= "+emitters.size());
        return sseEmitter;
    }

    @GetMapping(path="/send-sse/{text}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public  void sendEventToSubscribers(@PathVariable String text){

        emitters.forEach(e->{

                    try {var event= SseEmitter.event()
                            .data(text, MediaType.APPLICATION_JSON)
                            .id(String.valueOf(UUID.randomUUID()))
                            .name("TEST-EVENT");
                        e.send(event);


                    }catch (IOException ex){
                        emitters.remove(e);
                    }
                }
        );
        //System.out.println("I Work here");
    }

    // ------------------------------------------------------------------Мой Event
    @PostMapping(path="chat2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public  void sendEven(Person1Person2MSG person){

        /*emitters.forEach(e->{

                    try {var event= SseEmitter.event()
                            .data(text, MediaType.APPLICATION_JSON)
                            .id(String.valueOf(UUID.randomUUID()))
                            .name("TEST-EVENT");
                        e.send(event);


                    }catch (IOException ex){
                        emitters.remove(e);
                    }
                }
        );*/
        System.out.println("I Work here: "+person.getMessage());
    }
}
