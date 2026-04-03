package org.example.corepayuserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CorepayUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorepayUserServiceApplication.class, args);
    }

}
