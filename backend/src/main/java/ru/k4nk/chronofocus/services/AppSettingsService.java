package ru.k4nk.chronofocus.services;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties(prefix = "app")
@Data
public class AppSettingsService {
    private String promoSingUp;
    private String version;
}
