package ru.k4nk.chronofocus.http_api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;

@Builder
public record ApiBody(String apiVer, JsonNode payload, ru.k4nk.chronofocus.http_api.ApiBody.Status status,
                      String errorMessage) {

    public boolean checkVersion(ApiVersion apiVersion) {
        return this.apiVer().equals(apiVersion.toString());
    }

    public enum Status {
        OK,
        ERROR
    }
}

