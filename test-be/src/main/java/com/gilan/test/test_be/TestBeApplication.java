package com.gilan.test.test_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.gilan.test")
@EnableJpaRepositories("com.gilan.test.persistence.repository") 
@EntityScan("com.gilan.test.persistence.entity") 
public class TestBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestBeApplication.class, args);
	}

}
