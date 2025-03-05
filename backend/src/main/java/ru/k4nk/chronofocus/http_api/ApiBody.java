package ru.k4nk.chronofocus.http_api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Schema(description = "Обертка для API-ответов")
@NoArgsConstructor
public class ApiBody<T> {
    @Schema(description = "Полезная нагрузка")
    private List<T> payload;

    @Schema(description = "Дополнительные детали", example = "Запрос выполнен успешно")
    private String details;

    public ApiBody(List<T> payload) {
        this.payload = payload;
        this.details = "Запрос выполнен успешно";
    }

    public ApiBody(T payload) {
        this.payload = List.of(payload);
        this.details = "Запрос выполнен успешно";
    }

    public ApiBody(String details) {
        this.payload = null;
        this.details = details;
    }

    public ApiBody(List<T> payload, String details) {
        this.payload = payload;
        this.details = details;
    }
}

