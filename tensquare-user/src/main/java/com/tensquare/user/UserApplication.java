package com.tensquare.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import util.IdWorker;
import util.JwtUtil;

/**
 * @author Administrator
 */
@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
@EnableEurekaClient
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    /**
     * 初始化id生成
     */
    @Bean
    public IdWorker idWorkker() {
        return new IdWorker(1, 1);
    }

    /**
     * 初始化加密工具类
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 初始化jwt签发token
     */
    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }
}
