package com.ecommerce.totolo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class TotoloApplication {

	public static void main(String[] args) {
		SpringApplication.run(TotoloApplication.class, args);
	}

}
