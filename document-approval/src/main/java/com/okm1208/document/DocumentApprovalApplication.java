package com.okm1208.document;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class DocumentApprovalApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocumentApprovalApplication.class, args);
    }

}