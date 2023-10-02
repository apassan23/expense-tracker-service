package com.phoenix.expensetrackerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@EnableMongoAuditing
@EnableAspectJAutoProxy
public class ExpenseTrackerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerServiceApplication.class, args);
	}

}
