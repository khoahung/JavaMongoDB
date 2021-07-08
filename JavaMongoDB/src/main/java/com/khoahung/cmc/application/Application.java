package com.khoahung.cmc.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.khoahung.cmc.entity.LogData;
import com.khoahung.cmc.repository.JPALogRepository;


@SpringBootApplication
@ComponentScan(basePackages = "com.khoahung")
@EntityScan(basePackageClasses = LogData.class)
@EnableJpaRepositories(basePackageClasses = JPALogRepository.class)
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
