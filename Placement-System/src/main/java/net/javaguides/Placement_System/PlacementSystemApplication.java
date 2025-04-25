package net.javaguides.Placement_System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class PlacementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlacementSystemApplication.class, args);
	}

}
