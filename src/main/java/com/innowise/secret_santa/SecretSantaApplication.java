package com.innowise.secret_santa;

import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongock
@EnableScheduling
@PropertySource
        (
                value =
                        {
                                "classpath:email.properties",
                                "classpath:database.properties",
                                "classpath:jwt_token.properties"
                        }
        )
public class SecretSantaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecretSantaApplication.class, args);
    }
}