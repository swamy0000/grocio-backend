package com.grocio.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // అన్ని API లింక్స్ కి పర్మిషన్
                        .allowedOriginPatterns("*") // ఫ్లట్టర్ ఏ పోర్ట్ నుండి వచ్చినా అలో చేస్తుంది
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // బ్రౌజర్ పంపే OPTIONS (Preflight) కి అలో ఇస్తుంది
                        .allowedHeaders("*")
                        .allowCredentials(false); // సెక్యూరిటీ రీజన్స్ వల్ల ప్రస్తుతానికి ఇది false ఉంచడం బెస్ట్
            }
        };
    }
}