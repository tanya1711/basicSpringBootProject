package com.example.demo.service;

import com.example.demo.model.ForgotPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaMessagePublisher {

    @Autowired
    private KafkaTemplate<String,Object> template;

    public void sendMessageToTopic(String message) {
        CompletableFuture<SendResult<String, Object>> future = template.send("topicFromSB", message);
        future.whenComplete((result,ex)->{
            if(ex == null){
                System.out.println("Message sent "+message);
            }
            else {
                System.out.println("Message sending failed.............."+message);
            }
        });

    }

    public void sendEventsToTopic(ForgotPassword message) {
        CompletableFuture<SendResult<String, Object>> future = template.send("testTopic", message);
        future.whenComplete((result,ex)->{
            if(ex == null){
                System.out.println("Message sent "+message);
            }
            else {
                System.out.println("Message sending failed.............."+message);
            }
        });

    }
}
