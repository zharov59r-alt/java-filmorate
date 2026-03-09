package ru.yandex.practicum.filmorate.api;

import lombok.Getter;

import java.util.List;

@Getter
public class ErrorResponse {
    private String error;
    private List<String> details;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public ErrorResponse(String error, List<String> details) {
        this.error = error;
        this.details = details;
    }
}
