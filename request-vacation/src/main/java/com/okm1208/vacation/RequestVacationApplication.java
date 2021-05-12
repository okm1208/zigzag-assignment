package com.okm1208.vacation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author Nick (okm1208@gmail.com)
 * @created 2021-05-12
 */
@SpringBootApplication
public class RequestVacationApplication  extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(RequestVacationApplication.class, args);
    }
}
