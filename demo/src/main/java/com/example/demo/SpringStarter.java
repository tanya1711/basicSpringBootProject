package com.example.demo;

import com.example.demo.rep.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling

public class SpringStarter {

	public static long newtIME = 0;
	@Autowired
	private LoginRepository loginRepository;
	public static void main(String[] args) {

		MongoConnection mongoConnection = new MongoConnection();
		SpringApplication.run(SpringStarter.class, args);
		newtIME = System.currentTimeMillis();
		System.out.println(System.currentTimeMillis());

	}

//	@Scheduled(fixedDelay = 30_000)
//	public void clearFailedAttempts(){
//		loginRepository.deleteAll();
//	}

}
