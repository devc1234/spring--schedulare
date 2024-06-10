package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.Date;

@SpringBootApplication

public class SchedulareApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulareApplication.class, args);
	}
	

	@Scheduled(fixedRate = 2000)
	public void firstjob() {
		System.out.println("now current time"+new Date());
	}

}
