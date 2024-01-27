package com.example.demo.rep;

import com.example.demo.model.Login;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoginRepository extends MongoRepository<Login,String> {

}
