package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoConnection {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void checkConnection() {

        System.out.println(mongoTemplate.getCollectionNames());
    }

}
