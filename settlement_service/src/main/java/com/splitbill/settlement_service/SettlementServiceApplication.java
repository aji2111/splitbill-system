package com.splitbill.settlement_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SettlementServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                SettlementServiceApplication.class,
                args
        );
    }
}