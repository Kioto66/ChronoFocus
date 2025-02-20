package ru.k4nk.chronofocus.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    //TODO настроить доп CORS в application.yml
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Разрешить CORS для всех путей
                        .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173") // Разрешить запросы с этого origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Разрешить методы
                        .allowedHeaders("*") // Разрешить все заголовки
                        .allowCredentials(true); // Разрешить передачу куки и авторизационных данных
            }
        };
    }
}