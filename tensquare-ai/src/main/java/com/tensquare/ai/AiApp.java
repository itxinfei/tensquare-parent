package com.tensquare.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 人工智能
 */
@SpringBootApplication
@EnableScheduling
public class AiApp {
    public static void main(String[] args) {
        SpringApplication.run(AiApp.class, args);
    }
}
