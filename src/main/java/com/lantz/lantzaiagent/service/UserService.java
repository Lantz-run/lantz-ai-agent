package com.lantz.lantzaiagent.service;

import org.springframework.stereotype.Component;

@Component
public class UserService {
    public void createUser(String name) {
        System.out.println("Creating user: " + name);
    }
}
