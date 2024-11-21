package com.avirantEnterprises.information_collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.avirantEnterprises")
public class InformationCollectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(InformationCollectorApplication.class, args);
	}

}
