package dev.malay.productservicejuly26.controlleradvice;

import dev.malay.productservicejuly26.dtos.ExceptionDto;
import dev.malay.productservicejuly26.exceptions.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleProductNotFound(
            ProductNotFoundException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Product not found",
                exception.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleValidation(
            MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed",
                "Request contains invalid fields", request.getRequestURI(), fieldErrors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDto> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid request",
                "The product id must be a number", request.getRequestURI(), null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> handleIllegalArgument(
            IllegalArgumentException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid request",
                exception.getMessage(), request.getRequestURI(), null);
    }

    private ResponseEntity<ExceptionDto> buildResponse(
            HttpStatus status, String error, String message, String path, Map<String, String> fieldErrors) {
        ExceptionDto response = new ExceptionDto();
        response.setStatus(status.value());
        response.setError(error);
        response.setMessage(message);
        response.setPath(path);
        response.setFieldErrors(fieldErrors);
        return ResponseEntity.status(status).body(response);
    }
}
