package ru.k4nk.chronofocus.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.k4nk.chronofocus.http_api.ApiBody;

import java.util.ArrayList;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiBody<?>> handleAllExceptions(Exception ex, WebRequest request) {
        // Логирование ошибки
        ex.printStackTrace();

        // Создаем ответ с сообщением об ошибке
        ApiBody<?> responseBody = new ApiBody<>(new ArrayList<>());
        responseBody.setDetails("Внутренняя ошибка сервера: " + ex.getMessage());

        // Возвращаем статус 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }
}