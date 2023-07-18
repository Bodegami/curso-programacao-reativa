package br.com.bodegami.webfluxcourse.controler.exceptions;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler {


    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Mono<StandardError>> duplicateKeyException(
            DuplicateKeyException ex, ServerHttpRequest request) {

        Mono<StandardError> response = Mono.just(StandardError.builder()
                .timestamp(LocalDateTime.now())
                .path(request.getPath().toString())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(this.verifyDupKey(ex.getMessage()))
                .build()
        );

        return ResponseEntity.badRequest().body(response);
    }

    private String verifyDupKey(String message) {
        if (message.contains("email dup key")) {
            return "Email already registered!";
        }
        return "dup key exception";
    }

}
