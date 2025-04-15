package com.playfooty.backendCore.exception;

import com.playfooty.backendCore.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> generalException(Exception e) {
        return new ResponseEntity<>(
                ErrorResponseDTO.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(e.getMessage())
                        .error("Server error")
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> badRequestException (BadRequestException e) {
        return new ResponseEntity<>(
                ErrorResponseDTO.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage())
                        .error("Bad request")
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> notFoundException (NotFoundException e) {
        return new ResponseEntity<>(
                ErrorResponseDTO.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .error("Not found")
                        .message(e.getMessage())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> unauthorizedException (UnauthorizedException e) {
        return new ResponseEntity<>(
                ErrorResponseDTO.builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .error("Unauthorized")
                        .message(e.getMessage())
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponseDTO> handleForbiddenException (ForbiddenException e, WebRequest request) {
        return new ResponseEntity<>(
                ErrorResponseDTO.builder()
                        .status(HttpStatus.FORBIDDEN.value())
                        .error("Forbidden")
                        .message(e.getMessage())
                        .build(),
                HttpStatus.FORBIDDEN
        );
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> requestValidationException (MethodArgumentNotValidException e, WebRequest request) {
//
//        Map<String, String> errorMap = new HashMap<>();
//
//        e.getBindingResult().getFieldErrors().forEach(error -> {
//            errorMap.put(error.getField(), error.getDefaultMessage());
//        });
//
//        ErrorResponseDTO response = ErrorResponseDTO.builder()
//                .status(HttpStatus.BAD_REQUEST.value())
//                .error("Bad request")
//                .message(e.getMessage())
//                .build();
//
//        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
//    }

}
