package com.playfooty.backend_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
		"com.playfooty.backend_api",
		"com.playfooty.backendCore",
		"com.playfooty.userManagement"
})
@EnableJpaRepositories(basePackages = "com.playfooty")
@EntityScan(basePackages = "com.playfooty")
public class BackendApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApiApplication.class, args);
	}

}
