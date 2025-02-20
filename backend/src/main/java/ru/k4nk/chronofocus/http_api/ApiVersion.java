package ru.k4nk.chronofocus.http_api;

public enum ApiVersion {
    VERSION_1("1");

    private final String text;

    ApiVersion(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
