package org.example.order.exception;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j; // Логирование
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
      EntityNotFoundException ex, WebRequest request) {
    log.warn("Entity not found: {}", ex.getMessage());

    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
  }

  @ExceptionHandler(InsufficientInventoryException.class)
  public ResponseEntity<ErrorResponse> handleInsufficientInventoryException(
      InsufficientInventoryException ex, WebRequest request) {
    log.warn("Order rejected due to insufficient inventory: {}", ex.getMessage());

    return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
    log.error("Unexpected error in Order Service", ex);

    return buildResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "An unexpected error occurred. Please try again later.",
        request);
  }

  private ResponseEntity<ErrorResponse> buildResponse(
      HttpStatus status, String message, WebRequest request) {
    ErrorResponse errorResponse =
        new ErrorResponse(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getDescription(false).replace("uri=", ""));

    return new ResponseEntity<>(errorResponse, status);
  }
}
