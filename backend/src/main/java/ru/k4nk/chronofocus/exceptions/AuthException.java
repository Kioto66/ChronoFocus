package ru.k4nk.chronofocus.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(AuthException.class);

    public AuthException(String message) {
        log.warn(message);
    }

}
