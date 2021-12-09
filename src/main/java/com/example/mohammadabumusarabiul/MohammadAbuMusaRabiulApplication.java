package com.example.mohammadabumusarabiul;

import com.example.mohammadabumusarabiul.domainobject.SaleDO;
import com.example.mohammadabumusarabiul.util.LoggingInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class MohammadAbuMusaRabiulApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(MohammadAbuMusaRabiulApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new LoggingInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public ConcurrentHashMap<UUID, SaleDO> getConcurrentHashMapInstance(){
        return new ConcurrentHashMap<>();
    }
}
