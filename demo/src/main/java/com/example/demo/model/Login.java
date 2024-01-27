package com.example.demo.model;

import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "loginAttempts")
public class Login {
    private String email;
    private String password;
    private Integer attempts;
    private Long time;

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long timeInEpoc) {
        this.time = timeInEpoc;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
