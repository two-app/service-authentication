package com.two.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.two")
@EnableDiscoveryClient
@EnableTransactionManagement
public class AuthenticationApplication {

    public static void main(String[] args) {
        System.setProperty("org.jooq.no-logo", "true");
        SpringApplication.run(AuthenticationApplication.class, args);
    }

}
