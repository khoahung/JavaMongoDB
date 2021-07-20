package com.khoahung.cmc.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.khoahung")
public class ApplicationAPI {
	public static void main(String[] args) {
		SpringApplication.run(ApplicationAPI.class, args);
	}
}
