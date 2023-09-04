package com.adminParking.adminParking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.adminParking.adminParking"})
public class AdminParkingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminParkingApplication.class, args);
	}

}
