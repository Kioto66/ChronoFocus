package ru.k4nk.chronofocus.http_api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalTime;

public class LocalTimeJsonSerializer extends JsonSerializer<LocalTime> {
    @Override
    public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeStartObject();
            gen.writeNumberField("hours", value.getHour());
            gen.writeNumberField("minutes", value.getMinute());
            gen.writeNumberField("seconds", value.getSecond());
            gen.writeEndObject();
        }
    }
}