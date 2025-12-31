package org.example.inventory.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFound(
      EntityNotFoundException ex, WebRequest request) {
    log.warn("Entity not found: {}", ex.getMessage());

    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
  }

  @ExceptionHandler(InsufficientStockException.class)
  public ResponseEntity<ErrorResponse> handleInsufficientStock(
      InsufficientStockException ex, WebRequest request) {
    log.warn("Insufficient stock attempt: {}", ex.getMessage());

    return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
    log.error("Unexpected error occurred on request: {}", request.getDescription(false), ex);

    return buildResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Internal Server Error. Please contact support.",
        request);
  }

  private ResponseEntity<ErrorResponse> buildResponse(
      HttpStatus status, String message, WebRequest request) {
    ErrorResponse errorResponse =
        new ErrorResponse(
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getDescription(false).replace("uri=", ""));

    return new ResponseEntity<>(errorResponse, status);
  }
}
