package com.example.ViChat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ViChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ViChatApplication.class, args);
	}

}
