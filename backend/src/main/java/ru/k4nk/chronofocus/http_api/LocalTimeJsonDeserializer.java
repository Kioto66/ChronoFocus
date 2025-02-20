package ru.k4nk.chronofocus.http_api;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalTime;

public class LocalTimeJsonDeserializer extends JsonDeserializer<LocalTime> {
    @Override
    public LocalTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode jsonNode = parser.getCodec().readTree(parser);
        if (jsonNode.isEmpty()) {
            return null;
        }
        int hours = jsonNode.get("hours").asInt();
        int minutes = jsonNode.get("minutes").asInt();
        int seconds = jsonNode.has("seconds") ? jsonNode.get("seconds").asInt() : 0;

        return LocalTime.of(hours, minutes, seconds);
    }
}