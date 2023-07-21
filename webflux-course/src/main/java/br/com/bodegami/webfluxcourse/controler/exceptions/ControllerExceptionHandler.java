package br.com.bodegami.webfluxcourse.controler.exceptions;

import br.com.bodegami.webfluxcourse.service.exception.ObjectNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
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

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Mono<ValidationError>> webExchangeBindException(
            WebExchangeBindException ex, ServerHttpRequest request) {

        ValidationError error = new ValidationError(
                LocalDateTime.now(),
                request.getPath().toString(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation error",
                "Error on validation attributes"
        );

        for (FieldError x : ex.getBindingResult().getFieldErrors()) {
            error.addError(x.getField(), x.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(Mono.just(error));
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Mono<StandardError>> objectNotFoundException(
            ObjectNotFoundException ex, ServerHttpRequest request) {

        Mono<StandardError> response = Mono.just(StandardError.builder()
                .timestamp(LocalDateTime.now())
                .path(request.getPath().toString())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .build()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    private String verifyDupKey(String message) {
        if (message.contains("email dup key")) {
            return "Email already registered!";
        }
        return "dup key exception";
    }

}
