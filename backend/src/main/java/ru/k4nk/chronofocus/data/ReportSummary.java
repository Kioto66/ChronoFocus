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
public class ReportSummary {
    @JsonProperty("type")
    private static final String TYPE = "summary";

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime from;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime to;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd_HH:mm:ss")
    private LocalDateTime timestamp;

    private List<ReportEntry> body;

    @Getter
    @Setter
    @AllArgsConstructor
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
