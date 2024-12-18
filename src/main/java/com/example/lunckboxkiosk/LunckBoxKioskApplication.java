package com.example.lunckboxkiosk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LunckBoxKioskApplication {

    public static void main(String[] args) {
        SpringApplication.run(LunckBoxKioskApplication.class, args);
    }
}
