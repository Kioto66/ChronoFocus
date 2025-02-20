package ru.k4nk.chronofocus.http_api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Duration;

public class DurationJsonSerializer extends JsonSerializer<Duration> {
    @Override
    public void serialize(Duration value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeStartObject();
            long seconds = value.toSeconds();
            long hours = seconds / 3600;
            seconds = seconds - hours * 3600;
            long minutes = seconds / 60;
            seconds = seconds - minutes * 60;

            gen.writeNumberField("hours", hours);
            gen.writeNumberField("minutes", minutes);
            gen.writeNumberField("seconds", seconds);
            gen.writeEndObject();
        }
    }
}
