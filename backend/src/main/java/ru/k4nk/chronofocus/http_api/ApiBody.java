package ru.k4nk.chronofocus.http_api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Обертка для API-ответов")
public class ApiBody<T> {
    @Schema(description = "Полезная нагрузка")
    private final T payload;

    @Schema(description = "Дополнительные детали", example = "Запрос выполнен успешно")
    private final String details;

    public ApiBody(T payload) {
        this.payload = payload;
        this.details = "Запрос выполнен успешно";
    }

    public ApiBody(String details) {
        this.payload = null;
        this.details = details;
    }

    public ApiBody(T payload, String details) {
        this.payload = payload;
        this.details = details;
    }
}

