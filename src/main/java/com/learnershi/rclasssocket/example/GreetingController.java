package com.learnershi.rclasssocket.example;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/greetings")
    public Greet greet(HelloMessage message) throws Exception {
        Thread.sleep(1000);
        return new Greet("Hello, " + message.getName() + "!");
    }

    @MessageMapping("/getSomething")
    @SendTo("/something")
    public Greet message(HelloMessage message) throws Exception {
        Thread.sleep(1000);
        return new Greet(message.getName());
    }
}
