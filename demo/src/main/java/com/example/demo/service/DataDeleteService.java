package com.example.demo.service;

import com.example.demo.model.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DataDeleteService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public DataDeleteService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // This method will be executed every 30 seconds to delete data older than 30 seconds
    @Scheduled(fixedRate = 30000) // 30 seconds in milliseconds
    public void deleteOldData() {

        Long thresholdTime = System.currentTimeMillis() - 30000;
        mongoTemplate.remove(Query.query(Criteria.where("time").lt(thresholdTime)), Login.class);

        System.out.println("removedddd..............");
    }
}
