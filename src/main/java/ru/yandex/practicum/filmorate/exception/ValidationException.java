package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException  {

    private List<String> details;
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, List<String> details) {
        super(message);
        this.details = details;
    }

    public ValidationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
