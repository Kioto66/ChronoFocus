package ru.k4nk.chronofocus.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";


    private final JwtProvider jwtProvider;

    private final String[] permitAllPaths;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtFilter(JwtProvider jwtProvider, String[] permitAllPaths) {
        this.jwtProvider = jwtProvider;
        this.permitAllPaths = permitAllPaths;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestPath = request.getRequestURI();
        for (String pattern : permitAllPaths) {
            if (pathMatcher.match(pattern, requestPath)) {
                return true;
            }
        }
        return false;
//        return request.getRequestURI().startsWith("/auth/");
    }

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                 @NonNull FilterChain fc) throws IOException, ServletException {
        if (response.isCommitted()) {
            return;
        }

        final String token = getTokenFromRequest(request);
        if (token != null && jwtProvider.validateAccessToken(token)) {
            final Claims claims = jwtProvider.getAccessClaims(token);
            final JwtAuthentication jwtAuth = JwtAuthentication.generate(claims);
            jwtAuth.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtAuth);
        } else {
            log.debug("token {}", token);
            log.debug("jwtProvider.validateAccessToken(token) {}",
                    token != null ? jwtProvider.validateAccessToken(token) : "null");
            log.debug(request.getRequestURI());
            throw new AccessDeniedException("Access denied");
        }
        fc.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}