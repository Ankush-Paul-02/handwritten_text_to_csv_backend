package com.devmare.extract_text_image.exceptions;

import com.devmare.extract_text_image.payloads.AppApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<AppApiResponse> handleImageUploadException(
            FileUploadException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(AppApiResponse.builder()
                        .message(exception.getMessage())
                        .isSuccess(false)
                        .build()
                );
    }
}
