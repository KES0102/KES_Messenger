package org.example.springclasses.controller;

import org.example.springclasses.config.SpringConfig;
import org.example.springclasses.dao.PersonDAO;
import org.example.springclasses.models.Person1Person2MSG;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class PeopleController {



    private final PersonDAO personDAO;

    @Autowired
    private ApplicationContext context;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }


    //---------------------------------------------------------Login
    @GetMapping()
    public  String show(Model model){
        //model.addAttribute("people", personDAO.show());
        Person1Person2MSG person1Person2MSG =new Person1Person2MSG();

        model.addAttribute("person1Person2MSG", person1Person2MSG);
        return "index";
    }


    // --Chats Список
    @PostMapping("/person")
    public String login (Model model, @ModelAttribute("person") Person1Person2MSG person){

        person.setIdOne(personDAO.getIdFromEmail(person.getEmail()));
        //person.setIdOne(2);
        // Возвращает список всех людей
        List<Person1Person2MSG> list=personDAO.searchAll(person);
        System.out.println(list.size());
        model.addAttribute("personlist", list);
        return "person";
    }


    // --Запрос страницы с чатами--------------------------------------------------------------
    @PostMapping("/chat1")
    public String goChat(Model model, @ModelAttribute("person") Person1Person2MSG person){
        //System.out.println(person.toString());
        // По ключу person:
        //                 idOne, idTwo, message(после  кнопки send)



        if(person.getMessage()!=null){
            personDAO.saveMessage(person);

            SseController sseController=context.getBean(SseController.class);
            System.out.println(sseController.emitters.size());


            List<SseEmitter> emittersHere=sseController.getEmitters();

            emittersHere.forEach( e ->{

                        try {var event= SseEmitter.event()
                                .data(person.getMessage(), MediaType.APPLICATION_JSON)
                                .id(String.valueOf(UUID.randomUUID()))
                                .name(String.valueOf(person.getIdTwo()));
                            e.send(event);


                        }catch (IOException ex){
                            emittersHere.remove(e);
                        }
                    }
            );
            person.setMessage("");
        }
    List<Person1Person2MSG> list=personDAO.readTableAfterMessages(person);
    List<Person1Person2MSG> listTemp=new ArrayList<>(10);

        for (int i=list.size()-1; i>=0; i--) {
                listTemp.add(list.get(i));
                }

        //System.out.println("Размерность list: "+listTemp.size());
        model.addAttribute("person_list", listTemp);

        // По ключу 1: person - доступно : idOne(авторизовавшийся), idTwo, nameTwo
        // По ключу 2: person_2- доступно message и его отправитель

        //model.addAttribute("personChat", person);
        return "chat1";
        }

    //------------------------------------------------------------registration
    @GetMapping("/registration")
    public String registration(Model model){
        Person1Person2MSG person=new Person1Person2MSG();
        model.addAttribute("person", person);
        return "registration";
    }

    @PostMapping("/index")
    public String regIndex(Model model, @ModelAttribute("person") Person1Person2MSG person){
        personDAO.registration(person);
        model.addAttribute("person1Person2MSG", new Person1Person2MSG());

        return "index";
    }

}
