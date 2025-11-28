package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate; // 👈 Importa

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // 💡 Agrega este Bean para inyectar RestTemplate
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}