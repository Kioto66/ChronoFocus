package ru.k4nk.chronofocus.controllers;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.k4nk.chronofocus.http_api.ApiHelper;
import ru.k4nk.chronofocus.jwt.JwtProvider;

import java.time.LocalDateTime;
import java.util.Objects;

@Tag(name = "ChronoFocus API", description = "API для управления трекерами времени и категориями")
public abstract class AbstractController {
    private final JwtProvider jwtProvider;

    protected AbstractController(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    protected String getUserLoginFromJwt() {
        String authHeader = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest().getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtProvider.validateAccessToken(token)) {
                Claims claims = jwtProvider.getAccessClaims(token);
                return claims.getSubject();
            }
        }
        throw new SecurityException("Invalid or missing JWT token");
    }

    protected LocalDateTime parseDateTimeOrNow(String dateTimeStr, LocalDateTime now) {
        return (dateTimeStr == null) ? LocalDateTime.now() : ApiHelper.parseStringDateTime(dateTimeStr, false);
    }
}
