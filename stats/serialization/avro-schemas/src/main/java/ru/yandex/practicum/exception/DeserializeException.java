package ru.yandex.practicum.exception;

public class DeserializeException extends RuntimeException {
    public DeserializeException(String message) {
        super(message);
    }
}
