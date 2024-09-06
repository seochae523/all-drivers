package com.alldriver.alldriver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AlldriverApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlldriverApplication.class, args);
	}

}
