package com.alldriver.alldriver;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
@OpenAPIDefinition(servers = {@Server(url = "https://alldriver.co.kr", description = "https 전용 테스트"),
		@Server(url="http://localhost:8080", description = "로컬 호스트 테스트")})
@SpringBootApplication
@EnableAsync
public class AlldriverApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlldriverApplication.class, args);
	}

}
