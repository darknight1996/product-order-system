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
      final EntityNotFoundException exception, final WebRequest request) {
    final String message = exception.getMessage();
    final HttpStatus status = HttpStatus.NOT_FOUND;
    final String path = request.getDescription(false);

    final ErrorResponse errorResponse = createErrorResponse(status, message, path);

    return new ResponseEntity<>(errorResponse, status);
  }

  @ExceptionHandler(InsufficientInventoryException.class)
  public ResponseEntity<ErrorResponse> handleCreateOrderException(
      final InsufficientInventoryException exception, final WebRequest request) {
    final String message = exception.getMessage();
    final HttpStatus status = HttpStatus.CONFLICT;
    final String path = request.getDescription(false);

    final ErrorResponse errorResponse = createErrorResponse(status, message, path);

    return new ResponseEntity<>(errorResponse, status);
  }

  private ErrorResponse createErrorResponse(
      final HttpStatus status, final String message, final String path) {
    return new ErrorResponse(
        LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, path);
  }
}
