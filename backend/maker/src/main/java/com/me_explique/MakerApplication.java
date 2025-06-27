package com.me_explique.maker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.me_explique")
public class MakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MakerApplication.class, args);
	}

}
