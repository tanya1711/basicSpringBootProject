package com.example.demo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.internals.Topic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    public NewTopic createTopic(){
        return new NewTopic("testTopic",2, (short) 1);
    }

}
