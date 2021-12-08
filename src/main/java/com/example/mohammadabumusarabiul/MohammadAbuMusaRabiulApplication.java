package com.example.mohammadabumusarabiul;

import com.example.mohammadabumusarabiul.domainobject.SaleDO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class MohammadAbuMusaRabiulApplication {

    public static void main(String[] args) {
        SpringApplication.run(MohammadAbuMusaRabiulApplication.class, args);
    }

    @Bean
    public ConcurrentHashMap<UUID, SaleDO> getConcurrentHashMapInstance(){
        return new ConcurrentHashMap<>();
    }
}
