package ru.k4nk.chronofocus.jwt.dto;

import lombok.Getter;
import ru.k4nk.chronofocus.data.sys.User;

@Getter
public class JwtResponse {

    private final String type = "Bearer";

    private final String accessToken;

    private final String refreshToken;

    public JwtResponse(String accessToken, String refreshToken, User user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}