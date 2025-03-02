package ru.k4nk.chronofocus.jwt.dto;

import lombok.Data;

@Data
public class JwtRequest {

    private String login;
    private String password;

    @Override
    public String toString() {
        return "JwtRequest{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}