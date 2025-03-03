package ru.k4nk.chronofocus.services;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.k4nk.chronofocus.data.sys.Role;
import ru.k4nk.chronofocus.data.sys.RoleRepository;
import ru.k4nk.chronofocus.data.sys.User;
import ru.k4nk.chronofocus.exceptions.AuthException;
import ru.k4nk.chronofocus.jwt.JwtAuthentication;
import ru.k4nk.chronofocus.jwt.JwtProvider;
import ru.k4nk.chronofocus.jwt.dto.JwtRequest;
import ru.k4nk.chronofocus.jwt.dto.JwtResponse;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthService {

    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    private static final String ROLE_ADMIN = "ADMIN";
    private final RoleRepository roleRepository;


    private final PasswordEncoder passwordEncoder;

    public AuthService(UserService userService, JwtProvider jwtProvider, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;

        if (roleRepository.findByName(ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(ROLE_ADMIN));
        }
    }

    public JwtResponse login(String login, String password) {
        final User user = userService.findByLogin(login)
                .orElseThrow(() -> new AuthException("Пользователь " + login + " не найден"));

        if (passwordEncoder.matches(password, user.getPassword())){
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getLogin(), refreshToken);
            return new JwtResponse(accessToken, refreshToken, user);
        } else {
            throw new AuthException("Неправильный пароль для пользователя " + login);
        }
    }

    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
        return login(authRequest.getLogin(), authRequest.getPassword());
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.findByLogin(login)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getLogin(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken, user);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    public JwtResponse singUp(String login, String password) throws IllegalArgumentException, IllegalStateException {
        if(userService.findByLogin(login).isPresent()) {
            throw new IllegalStateException("Пользователь " + login + " уже зарегистрирован в системе");
        }

        String hashedPassword = passwordEncoder.encode(password);
        userService.createUser(login, hashedPassword, ROLE_ADMIN);
        return login(login, password);
    }
}