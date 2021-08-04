package com.enhui.activiti;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author 胡恩会
 * @Date 2021/8/4 19:49
 **/
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Activiti6Application {

    public static void main(String[] args) {
        SpringApplication.run(Activiti6Application.class, args);
    }

}
