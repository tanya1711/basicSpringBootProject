package com.example.demo.service;

import com.example.demo.model.ForgotPassword;
import org.apache.juli.logging.LogFactory;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

import javax.management.Notification;

@Service
public class KafkaConsumer {

    @Autowired
    private ServiceClass serviceClass;
    Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "testTopic", groupId = "ct-1")
    public void listenMessage(ForgotPassword message) {
        Boolean success = serviceClass.changePassword(message);
        System.out.println(success);
        System.out.println("hello from consumer");
        logger.info("consumer consume message {} ", message.toString());
    }

//    @Bean
//    public ConsumerFactory<String, Topic_Test> DtoConsumerTest()
//    {
//        ErrorHandlingDeserializer<Notification> errorHandlingDeserializer;
//        errorHandlingDeserializer = new ErrorHandlingDeserializer<>(new JsonDeserializer<>(Topic_Test.class));
//        ...
//        ...
//        ...
//        return new DefaultKafkaConsumerFactory<>(
//                props, new StringDeserializer(),
//                errorHandlingDeserializer);
//    }

}
