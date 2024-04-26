package com.spring.loginroles.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Aquí debes especificar el patrón de URL al que quieres aplicar CORS
                .allowedOrigins("http://localhost:63342") // Aquí debes especificar el origen permitido
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Aquí debes especificar los métodos HTTP permitidos
                .allowedHeaders("*"); // Aquí debes especificar los encabezados permitidos
    }
}
