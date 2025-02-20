package ru.k4nk.chronofocus.controllers;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.k4nk.chronofocus.AppConfig;

@RestController
@AllArgsConstructor
public class SystemController {
    private final ResourceLoader resourceLoader;
    private final AppConfig appConfig;

    @GetMapping("/help")
    public ResponseEntity<Resource> getHelp() {
        try {
            Resource resource = resourceLoader.getResource("classpath:/static/help.html");
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/version")
    public String getVersion() {
        return appConfig.getVersion();
    }
}
