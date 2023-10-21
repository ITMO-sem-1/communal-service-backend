package ru.itmo.communal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootApplication
public class CommunalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommunalServiceApplication.class, args);
	}

}
