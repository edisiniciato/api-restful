package com.edi.apirestful;

import com.edi.apirestful.service.FilmeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiRestfulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiRestfulApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(FilmeService service) {
        return args -> {
            service.insertToCsv(service);
        };
    }
}
