package ru.k4nk.chronofocus.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.k4nk.chronofocus.exceptions.AuthException;
import ru.k4nk.chronofocus.jwt.dto.JwtRequest;
import ru.k4nk.chronofocus.jwt.dto.JwtResponse;
import ru.k4nk.chronofocus.jwt.dto.JwtSingUpBody;
import ru.k4nk.chronofocus.jwt.dto.RefreshJwtRequest;
import ru.k4nk.chronofocus.services.AppSettingsService;
import ru.k4nk.chronofocus.services.AuthService;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AppSettingsService appSettingsService;

    @PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        try {
            final JwtResponse token = authService.login(authRequest);
            return ResponseEntity.ok(token);
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        try {
            final JwtResponse token = authService.refresh(request.getRefreshToken());
            return ResponseEntity.ok(token);
        } catch (ru.k4nk.chronofocus.exceptions.AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("sing-up")
    public ResponseEntity<JwtResponse> singUp(@RequestBody JwtSingUpBody request) {
        if (request.promo() == null || !request.promo().equals(appSettingsService.getPromoSingUp())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        try {
            authService.singUp(request.login(), request.password());
            return ResponseEntity.ok(authService.login(request.login(), request.password()));
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}