package com.me_explique;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.me_explique")
@OpenAPIDefinition(info =
@Info(title = "API Explique Me", version = "0.0.1"))
public class MakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MakerApplication.class, args);
	}

}
