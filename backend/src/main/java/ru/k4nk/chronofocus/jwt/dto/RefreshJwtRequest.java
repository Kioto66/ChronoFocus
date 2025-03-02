package ru.k4nk.chronofocus.jwt.dto;

public class RefreshJwtRequest {

    public String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}