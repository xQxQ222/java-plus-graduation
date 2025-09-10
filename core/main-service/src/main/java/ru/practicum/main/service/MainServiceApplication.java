package ru.practicum.main.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.feign.client.StatsFeignClient;

@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan({"ru.practicum.main.service", "client"})
@EnableFeignClients(clients = StatsFeignClient.class)
public class MainServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainServiceApplication.class, args);
    }
}
