package com.whitestone.hrms.controller;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping("/**")
        .allowedOrigins("*") // Allows all origins
        .allowedMethods("*")
        .allowedHeaders("*")
        .allowCredentials(false); // Credentials are not allowed
    }
}
