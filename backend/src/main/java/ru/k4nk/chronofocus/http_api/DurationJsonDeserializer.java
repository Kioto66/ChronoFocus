package ru.k4nk.chronofocus.http_api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class DurationJsonDeserializer  extends JsonDeserializer<Duration> {
    @Override
    public Duration deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode jsonNode = parser.getCodec().readTree(parser);
        if (jsonNode.isEmpty()) {
            return null;
        }
        long hours = jsonNode.get("hours").asLong();
        long minutes = jsonNode.get("minutes").asLong();
        long seconds = jsonNode.has("seconds") ? jsonNode.get("seconds").asLong() : 0;

        return Duration.of((seconds + minutes * 60 + hours * 3600), ChronoUnit.SECONDS);
    }
}