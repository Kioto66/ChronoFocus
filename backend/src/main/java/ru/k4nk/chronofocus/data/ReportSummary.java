package ru.k4nk.chronofocus.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.k4nk.chronofocus.http_api.DurationJsonDeserializer;
import ru.k4nk.chronofocus.http_api.DurationJsonSerializer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "Сводка по времени за период")
public class ReportSummary {
    @Schema(description = "Тип отчета", example = "summary")
    @JsonProperty("type")
    private static final String TYPE = "summary";

    @Schema(description = "Начальная дата периода", example = "2025-02-18 00:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime from;

    @Schema(description = "Конечная дата периода", example = "2025-02-18 23:59:59")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime to;

    @Schema(description = "Дата формирования отчета", example = "2025-02-18 15:45:30")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd_HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "Список записей отчета")
    private List<ReportEntry> body;

    @Getter
    @Setter
    @AllArgsConstructor
    @Schema(description = "Запись отчета по категории")
    public static class ReportEntry {
        @JsonUnwrapped
        private Category category;

        @Schema(description = "Duration formatted as {hours, minutes, seconds}",
                example = "{\"hours\":1, \"minutes\":30, \"seconds\":15}")
        @JsonSerialize(using = DurationJsonSerializer.class)
        @JsonDeserialize(using = DurationJsonDeserializer.class)
        private Duration duration;
    }
}
