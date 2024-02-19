package com.CNPM.QLNT.configure;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Cors implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5174") // cho phep front fetch ve
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
