package ru.yandex.practicum.feign.client.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.feign.client.decoder.exception.EntityNotFoundException;
import ru.yandex.practicum.feign.client.decoder.exception.EntityValidationException;
import ru.yandex.practicum.feign.client.decoder.exception.InternalServerException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        return switch (response.status()) {
            case 400 -> new EntityValidationException("Ошибка валидации в методе " + s);
            case 404 -> new EntityNotFoundException("Сущность не найдена. Метод: " + s);
            case 500 -> new InternalServerException("Ошибка на стороне сервера в методе " + s);
            default -> defaultDecoder.decode(s, response);
        };
    }
}
