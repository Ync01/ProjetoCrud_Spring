package br.gov.sp.cps.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainProgram {

	// Rodar pelo terminal : .\mvnw spring-boot:run "-Dspring-boot.run.profiles=dev"
	public static void main(String[] args) {
		SpringApplication.run(MainProgram.class, args);

	}

}
