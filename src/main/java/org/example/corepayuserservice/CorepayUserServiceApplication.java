package org.example.corepayuserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = {
        "org.example.corepayuserservice",
        "org.example.corepaycommon"
})
public class CorepayUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorepayUserServiceApplication.class, args);
    }

}
