package com.example.mohammadabumusarabiul;

import com.example.mohammadabumusarabiul.domainobject.SaleDO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class MohammadabumusarabiulApplication {

    public static void main(String[] args) {
        SpringApplication.run(MohammadabumusarabiulApplication.class, args);
    }

    @Bean
    public ConcurrentHashMap<UUID, SaleDO> getConcurrentHashMapInstance(){
        return new ConcurrentHashMap<>();
    }
}
