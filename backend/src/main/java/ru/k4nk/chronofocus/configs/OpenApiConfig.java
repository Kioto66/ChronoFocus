package ru.k4nk.chronofocus.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Alexander",
                        email = "av@k4nk.ru"
                ),
                description = "OpenAPI documentation for ChronoFocus",
                title = "OpenAPI specification ChronoFocus",
                version = "1.0"
        ),
        servers = {@Server(
                description = "Local ENV",
                url = "http://localhost:8080"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "http://127.0.0.1:8080"
                )
        }
)
public class OpenApiConfig {
}
