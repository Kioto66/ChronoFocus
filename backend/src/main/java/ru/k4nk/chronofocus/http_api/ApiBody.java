package ru.k4nk.chronofocus.http_api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Builder;


public record ApiBody(JsonNode payload, String details) {
    public ApiBody {
    }

    public ApiBody(JsonNode payload) {
        this(payload, "");

    }

    public ApiBody(String details) {
        this(null, details);
    }
}

