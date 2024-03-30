package com.Fujitsu.Fooddelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.Fujitsu.Fooddelivery.model")
public class FooddeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(FooddeliveryApplication.class, args);
    }
}
