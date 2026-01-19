package com.syook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmitterServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmitterServiceApplication.class, args);
	}

}
