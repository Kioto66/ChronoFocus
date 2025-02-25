package ru.k4nk.chronofocus.controllers;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import ru.k4nk.chronofocus.AppConfig;

@RestController
@AllArgsConstructor
public class SystemController {
    private final AppConfig appConfig;

    @GetMapping("/help")
    public RedirectView getHelp() {
        return new RedirectView("/swagger-ui/index.html");
    }

    @GetMapping("/version")
    public String getVersion() {
        return appConfig.getVersion();
    }
}
