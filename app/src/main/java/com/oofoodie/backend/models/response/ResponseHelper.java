package com.oofoodie.backend.models.response;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ResponseHelper {

    public static <T> ErrorResponse<T> error(HttpStatus status, T message) {
        ErrorResponse<T> response = new ErrorResponse<>();

        response.setTimestamp(LocalDateTime.now());
        response.setCode(status.value());
        response.setStatus(status.name());
        response.setMessage(message);

        return response;
    }
}
