package org.example.order.exception;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
      EntityNotFoundException exception, WebRequest request) {
    String message = exception.getMessage();
    HttpStatus status = HttpStatus.NOT_FOUND;
    String path = request.getDescription(false);

    ErrorResponse errorResponse = createErrorResponse(status, message, path);

    return new ResponseEntity<>(errorResponse, status);
  }

  @ExceptionHandler(InsufficientInventoryException.class)
  public ResponseEntity<ErrorResponse> handleCreateOrderException(
      InsufficientInventoryException exception, WebRequest request) {
    String message = exception.getMessage();
    HttpStatus status = HttpStatus.CONFLICT;
    String path = request.getDescription(false);

    ErrorResponse errorResponse = createErrorResponse(status, message, path);

    return new ResponseEntity<>(errorResponse, status);
  }

  private ErrorResponse createErrorResponse(HttpStatus status, String message, String path) {
    return new ErrorResponse(
        LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, path);
  }
}
