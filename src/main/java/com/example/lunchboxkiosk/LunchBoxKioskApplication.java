package com.example.lunchboxkiosk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LunchBoxKioskApplication {

    public static void main(String[] args) {
        SpringApplication.run(LunchBoxKioskApplication.class, args);
    }
}
