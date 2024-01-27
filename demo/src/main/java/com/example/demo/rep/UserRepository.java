package com.example.demo.rep;

import com.example.demo.model.Form;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Form,String> {

    public Form findByName(String name);

    public Form findByEmail(String email);

//    public Form findById(String id);


}
