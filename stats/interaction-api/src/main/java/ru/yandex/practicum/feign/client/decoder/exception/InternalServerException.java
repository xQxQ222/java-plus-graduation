package ru.yandex.practicum.feign.client.decoder.exception;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }
}
