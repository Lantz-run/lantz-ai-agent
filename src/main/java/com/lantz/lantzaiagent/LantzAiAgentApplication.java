package com.lantz.lantzaiagent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.lantz.lantzaiagent.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class LantzAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(LantzAiAgentApplication.class, args);
    }

}
