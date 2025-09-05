package ru.yandex.practicum.feign.client.config;

import feign.Feign;
import org.springframework.context.annotation.Bean;
import ru.yandex.practicum.feign.client.decoder.FeignErrorDecoder;

public class FeignConfig {
    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .errorDecoder(new FeignErrorDecoder());
    }
}
