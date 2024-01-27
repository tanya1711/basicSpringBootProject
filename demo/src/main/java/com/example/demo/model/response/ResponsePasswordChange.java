package com.example.demo.model.response;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public class ResponsePasswordChange {

    private String userName;
    private String newPassword;
    private Message responseMessage;


    public static class Message{
        private String statusCode;
        private String message;

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

        public String getStatusCode() {
            return statusCode;
        }
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setResponseMessage(Message responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Message getResponseMessage() {
        return responseMessage;
    }
}
